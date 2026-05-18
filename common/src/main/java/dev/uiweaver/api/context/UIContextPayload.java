package dev.uiweaver.api.context;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

public sealed interface UIContextPayload
        permits UIContextPayload.None, UIContextPayload.BlockContext, UIContextPayload.ItemContext {

    record None() implements UIContextPayload {}

    record BlockContext(
            ResourceKey<Level> dimension,
            BlockPos pos,
            Class<?> blockEntityClass,
            int maxDistance
    ) implements UIContextPayload {

        public BlockContext(ResourceKey<Level> dimension, BlockPos pos, Class<?> blockEntityClass) {
            this(dimension, pos, blockEntityClass, 8);
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeResourceLocation(dimension.location());
            buf.writeBlockPos(pos);
            buf.writeUtf(blockEntityClass.getName());
            buf.writeInt(maxDistance);
        }

        public static BlockContext decode(FriendlyByteBuf buf) {
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, buf.readResourceLocation());
            BlockPos pos = buf.readBlockPos();
            String className = buf.readUtf();
            int dist = buf.readInt();
            try {
                return new BlockContext(dim, pos, Class.forName(className), dist);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Unknown BlockEntity class in UIContextPayload: " + className);
            }
        }
    }

    record ItemContext(InteractionHand hand) implements UIContextPayload {

        public void encode(FriendlyByteBuf buf) {
            buf.writeEnum(hand);
        }

        public static ItemContext decode(FriendlyByteBuf buf) {
            return new ItemContext(buf.readEnum(InteractionHand.class));
        }
    }

    UIContextPayload NONE = new None();

    static UIContextPayload forBlock(ResourceKey<Level> dimension, BlockPos pos, Class<?> blockEntityClass) {
        return new BlockContext(dimension, pos, blockEntityClass);
    }

    static UIContextPayload forBlock(ResourceKey<Level> dimension, BlockPos pos, Class<?> blockEntityClass, int maxDistance) {
        return new BlockContext(dimension, pos, blockEntityClass, maxDistance);
    }

    static UIContextPayload forItem(InteractionHand hand) {
        return new ItemContext(hand);
    }

    static void encode(UIContextPayload payload, FriendlyByteBuf buf) {
        if (payload instanceof BlockContext bc) {
            buf.writeByte(1);
            bc.encode(buf);
        } else if (payload instanceof ItemContext ic) {
            buf.writeByte(2);
            ic.encode(buf);
        } else {
            buf.writeByte(0);
        }
    }

    static UIContextPayload decode(FriendlyByteBuf buf) {
        int type = buf.readByte();
        if (type == 1) return BlockContext.decode(buf);
        if (type == 2) return ItemContext.decode(buf);
        return NONE;
    }
}