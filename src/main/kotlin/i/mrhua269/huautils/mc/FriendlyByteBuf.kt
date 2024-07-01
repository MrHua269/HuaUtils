package i.mrhua269.i.mrhua269.huautils.mc

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import io.netty.util.ByteProcessor
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.channels.GatheringByteChannel
import java.nio.channels.ScatteringByteChannel
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*


class FriendlyByteBuf(private val source: ByteBuf) : ByteBuf() {
    fun readVarInt(): Int {
        var i = 0
        var j = 0

        var b0: Byte

        do {
            b0 = this.readByte()
            i = i or ((b0.toInt() and 127) shl (j++ * 7))
            if (j > 5) {
                throw RuntimeException("VarInt too big")
            }
        } while ((b0.toInt() and 128) == 128)

        return i
    }

    fun readVarLong(): Long {
        var i = 0L
        var j = 0

        var b0: Byte

        do {
            b0 = this.readByte()
            i = i or ((b0.toInt() and 127).toLong() shl (j++ * 7))
            if (j > 10) {
                throw RuntimeException("VarLong too big")
            }
        } while ((b0.toInt() and 128) == 128)

        return i
    }

    fun writeUUID(uuid: UUID): FriendlyByteBuf {
        this.writeLong(uuid.mostSignificantBits)
        this.writeLong(uuid.leastSignificantBits)
        return this
    }

    fun readUUID(): UUID {
        return UUID(this.readLong(), this.readLong())
    }

    fun writeVarInt(value: Int): FriendlyByteBuf {
        var value = value
        while ((value and -128) != 0) {
            this.writeByte(value and 127 or 128)
            value = value ushr 7
        }

        this.writeByte(value)
        return this
    }

    fun writeVarLong(value: Long): FriendlyByteBuf {
        var value = value
        while ((value and -128L) != 0L) {
            this.writeByte((value and 127L).toInt() or 128)
            value = value ushr 7
        }

        this.writeByte(value.toInt())
        return this
    }

    @JvmOverloads
    fun readUtf(maxLength: Int = 32767): String {
        val j = this.readVarInt()

        if (j > maxLength * 4) {
            throw DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLength * 4 + ")")
        } else if (j < 0) {
            throw DecoderException("The received encoded string buffer length is less than zero! Weird string!")
        } else {
            val s = this.toString(this.readerIndex(), j, StandardCharsets.UTF_8)

            this.readerIndex(this.readerIndex() + j)
            if (s.length > maxLength) {
                throw DecoderException("The received string length is longer than maximum allowed ($j > $maxLength)")
            } else {
                return s
            }
        }
    }

    @JvmOverloads
    fun writeUtf(string: String, maxLength: Int = 32767): FriendlyByteBuf {
        val abyte = string.toByteArray(StandardCharsets.UTF_8)

        if (abyte.size > maxLength) {
            throw EncoderException("String too big (was " + abyte.size + " bytes encoded, max " + maxLength + ")")
        } else {
            this.writeVarInt(abyte.size)
            this.writeBytes(abyte)
            return this
        }
    }

    override fun capacity(): Int {
        return source.capacity()
    }

    override fun capacity(i: Int): ByteBuf {
        return source.capacity(i)
    }

    override fun maxCapacity(): Int {
        return source.maxCapacity()
    }

    override fun alloc(): ByteBufAllocator {
        return source.alloc()
    }

    override fun order(): ByteOrder {
        return source.order()
    }

    override fun order(byteorder: ByteOrder): ByteBuf {
        return source.order(byteorder)
    }

    override fun unwrap(): ByteBuf {
        return source.unwrap()
    }

    override fun isDirect(): Boolean {
        return source.isDirect
    }

    override fun isReadOnly(): Boolean {
        return source.isReadOnly
    }

    override fun asReadOnly(): ByteBuf {
        return source.asReadOnly()
    }

    override fun readerIndex(): Int {
        return source.readerIndex()
    }

    override fun readerIndex(i: Int): ByteBuf {
        return source.readerIndex(i)
    }

    override fun writerIndex(): Int {
        return source.writerIndex()
    }

    override fun writerIndex(i: Int): ByteBuf {
        return source.writerIndex(i)
    }

    override fun setIndex(i: Int, j: Int): ByteBuf {
        return source.setIndex(i, j)
    }

    override fun readableBytes(): Int {
        return source.readableBytes()
    }

    override fun writableBytes(): Int {
        return source.writableBytes()
    }

    override fun maxWritableBytes(): Int {
        return source.maxWritableBytes()
    }

    override fun isReadable(): Boolean {
        return source.isReadable
    }

    override fun isReadable(i: Int): Boolean {
        return source.isReadable(i)
    }

    override fun isWritable(): Boolean {
        return source.isWritable
    }

    override fun isWritable(i: Int): Boolean {
        return source.isWritable(i)
    }

    override fun clear(): ByteBuf {
        return source.clear()
    }

    override fun markReaderIndex(): ByteBuf {
        return source.markReaderIndex()
    }

    override fun resetReaderIndex(): ByteBuf {
        return source.resetReaderIndex()
    }

    override fun markWriterIndex(): ByteBuf {
        return source.markWriterIndex()
    }

    override fun resetWriterIndex(): ByteBuf {
        return source.resetWriterIndex()
    }

    override fun discardReadBytes(): ByteBuf {
        return source.discardReadBytes()
    }

    override fun discardSomeReadBytes(): ByteBuf {
        return source.discardSomeReadBytes()
    }

    override fun ensureWritable(i: Int): ByteBuf {
        return source.ensureWritable(i)
    }

    override fun ensureWritable(i: Int, flag: Boolean): Int {
        return source.ensureWritable(i, flag)
    }

    override fun getBoolean(i: Int): Boolean {
        return source.getBoolean(i)
    }

    override fun getByte(i: Int): Byte {
        return source.getByte(i)
    }

    override fun getUnsignedByte(i: Int): Short {
        return source.getUnsignedByte(i)
    }

    override fun getShort(i: Int): Short {
        return source.getShort(i)
    }

    override fun getShortLE(i: Int): Short {
        return source.getShortLE(i)
    }

    override fun getUnsignedShort(i: Int): Int {
        return source.getUnsignedShort(i)
    }

    override fun getUnsignedShortLE(i: Int): Int {
        return source.getUnsignedShortLE(i)
    }

    override fun getMedium(i: Int): Int {
        return source.getMedium(i)
    }

    override fun getMediumLE(i: Int): Int {
        return source.getMediumLE(i)
    }

    override fun getUnsignedMedium(i: Int): Int {
        return source.getUnsignedMedium(i)
    }

    override fun getUnsignedMediumLE(i: Int): Int {
        return source.getUnsignedMediumLE(i)
    }

    override fun getInt(i: Int): Int {
        return source.getInt(i)
    }

    override fun getIntLE(i: Int): Int {
        return source.getIntLE(i)
    }

    override fun getUnsignedInt(i: Int): Long {
        return source.getUnsignedInt(i)
    }

    override fun getUnsignedIntLE(i: Int): Long {
        return source.getUnsignedIntLE(i)
    }

    override fun getLong(i: Int): Long {
        return source.getLong(i)
    }

    override fun getLongLE(i: Int): Long {
        return source.getLongLE(i)
    }

    override fun getChar(i: Int): Char {
        return source.getChar(i)
    }

    override fun getFloat(i: Int): Float {
        return source.getFloat(i)
    }

    override fun getDouble(i: Int): Double {
        return source.getDouble(i)
    }

    override fun getBytes(i: Int, bytebuf: ByteBuf): ByteBuf {
        return source.getBytes(i, bytebuf)
    }

    override fun getBytes(i: Int, bytebuf: ByteBuf, j: Int): ByteBuf {
        return source.getBytes(i, bytebuf, j)
    }

    override fun getBytes(i: Int, bytebuf: ByteBuf, j: Int, k: Int): ByteBuf {
        return source.getBytes(i, bytebuf, j, k)
    }

    override fun getBytes(i: Int, abyte: ByteArray): ByteBuf {
        return source.getBytes(i, abyte)
    }

    override fun getBytes(i: Int, abyte: ByteArray, j: Int, k: Int): ByteBuf {
        return source.getBytes(i, abyte, j, k)
    }

    override fun getBytes(i: Int, bytebuffer: ByteBuffer): ByteBuf {
        return source.getBytes(i, bytebuffer)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, outputstream: OutputStream, j: Int): ByteBuf {
        return source.getBytes(i, outputstream, j)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, gatheringbytechannel: GatheringByteChannel, j: Int): Int {
        return source.getBytes(i, gatheringbytechannel, j)
    }

    @Throws(IOException::class)
    override fun getBytes(i: Int, filechannel: FileChannel, j: Long, k: Int): Int {
        return source.getBytes(i, filechannel, j, k)
    }

    override fun getCharSequence(i: Int, j: Int, charset: Charset): CharSequence {
        return source.getCharSequence(i, j, charset)
    }

    override fun setBoolean(i: Int, flag: Boolean): ByteBuf {
        return source.setBoolean(i, flag)
    }

    override fun setByte(i: Int, j: Int): ByteBuf {
        return source.setByte(i, j)
    }

    override fun setShort(i: Int, j: Int): ByteBuf {
        return source.setShort(i, j)
    }

    override fun setShortLE(i: Int, j: Int): ByteBuf {
        return source.setShortLE(i, j)
    }

    override fun setMedium(i: Int, j: Int): ByteBuf {
        return source.setMedium(i, j)
    }

    override fun setMediumLE(i: Int, j: Int): ByteBuf {
        return source.setMediumLE(i, j)
    }

    override fun setInt(i: Int, j: Int): ByteBuf {
        return source.setInt(i, j)
    }

    override fun setIntLE(i: Int, j: Int): ByteBuf {
        return source.setIntLE(i, j)
    }

    override fun setLong(i: Int, j: Long): ByteBuf {
        return source.setLong(i, j)
    }

    override fun setLongLE(i: Int, j: Long): ByteBuf {
        return source.setLongLE(i, j)
    }

    override fun setChar(i: Int, j: Int): ByteBuf {
        return source.setChar(i, j)
    }

    override fun setFloat(i: Int, f: Float): ByteBuf {
        return source.setFloat(i, f)
    }

    override fun setDouble(i: Int, d0: Double): ByteBuf {
        return source.setDouble(i, d0)
    }

    override fun setBytes(i: Int, bytebuf: ByteBuf): ByteBuf {
        return source.setBytes(i, bytebuf)
    }

    override fun setBytes(i: Int, bytebuf: ByteBuf, j: Int): ByteBuf {
        return source.setBytes(i, bytebuf, j)
    }

    override fun setBytes(i: Int, bytebuf: ByteBuf, j: Int, k: Int): ByteBuf {
        return source.setBytes(i, bytebuf, j, k)
    }

    override fun setBytes(i: Int, abyte: ByteArray): ByteBuf {
        return source.setBytes(i, abyte)
    }

    override fun setBytes(i: Int, abyte: ByteArray, j: Int, k: Int): ByteBuf {
        return source.setBytes(i, abyte, j, k)
    }

    override fun setBytes(i: Int, bytebuffer: ByteBuffer): ByteBuf {
        return source.setBytes(i, bytebuffer)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, inputstream: InputStream, j: Int): Int {
        return source.setBytes(i, inputstream, j)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, scatteringbytechannel: ScatteringByteChannel, j: Int): Int {
        return source.setBytes(i, scatteringbytechannel, j)
    }

    @Throws(IOException::class)
    override fun setBytes(i: Int, filechannel: FileChannel, j: Long, k: Int): Int {
        return source.setBytes(i, filechannel, j, k)
    }

    override fun setZero(i: Int, j: Int): ByteBuf {
        return source.setZero(i, j)
    }

    override fun setCharSequence(i: Int, charsequence: CharSequence, charset: Charset): Int {
        return source.setCharSequence(i, charsequence, charset)
    }

    override fun readBoolean(): Boolean {
        return source.readBoolean()
    }

    override fun readByte(): Byte {
        return source.readByte()
    }

    override fun readUnsignedByte(): Short {
        return source.readUnsignedByte()
    }

    override fun readShort(): Short {
        return source.readShort()
    }

    override fun readShortLE(): Short {
        return source.readShortLE()
    }

    override fun readUnsignedShort(): Int {
        return source.readUnsignedShort()
    }

    override fun readUnsignedShortLE(): Int {
        return source.readUnsignedShortLE()
    }

    override fun readMedium(): Int {
        return source.readMedium()
    }

    override fun readMediumLE(): Int {
        return source.readMediumLE()
    }

    override fun readUnsignedMedium(): Int {
        return source.readUnsignedMedium()
    }

    override fun readUnsignedMediumLE(): Int {
        return source.readUnsignedMediumLE()
    }

    override fun readInt(): Int {
        return source.readInt()
    }

    override fun readIntLE(): Int {
        return source.readIntLE()
    }

    override fun readUnsignedInt(): Long {
        return source.readUnsignedInt()
    }

    override fun readUnsignedIntLE(): Long {
        return source.readUnsignedIntLE()
    }

    override fun readLong(): Long {
        return source.readLong()
    }

    override fun readLongLE(): Long {
        return source.readLongLE()
    }

    override fun readChar(): Char {
        return source.readChar()
    }

    override fun readFloat(): Float {
        return source.readFloat()
    }

    override fun readDouble(): Double {
        return source.readDouble()
    }

    override fun readBytes(i: Int): ByteBuf {
        return source.readBytes(i)
    }

    override fun readSlice(i: Int): ByteBuf {
        return source.readSlice(i)
    }

    override fun readRetainedSlice(i: Int): ByteBuf {
        return source.readRetainedSlice(i)
    }

    override fun readBytes(bytebuf: ByteBuf): ByteBuf {
        return source.readBytes(bytebuf)
    }

    override fun readBytes(bytebuf: ByteBuf, i: Int): ByteBuf {
        return source.readBytes(bytebuf, i)
    }

    override fun readBytes(bytebuf: ByteBuf, i: Int, j: Int): ByteBuf {
        return source.readBytes(bytebuf, i, j)
    }

    override fun readBytes(abyte: ByteArray): ByteBuf {
        return source.readBytes(abyte)
    }

    override fun readBytes(abyte: ByteArray, i: Int, j: Int): ByteBuf {
        return source.readBytes(abyte, i, j)
    }

    override fun readBytes(bytebuffer: ByteBuffer): ByteBuf {
        return source.readBytes(bytebuffer)
    }

    @Throws(IOException::class)
    override fun readBytes(outputstream: OutputStream, i: Int): ByteBuf {
        return source.readBytes(outputstream, i)
    }

    @Throws(IOException::class)
    override fun readBytes(gatheringbytechannel: GatheringByteChannel, i: Int): Int {
        return source.readBytes(gatheringbytechannel, i)
    }

    override fun readCharSequence(i: Int, charset: Charset): CharSequence {
        return source.readCharSequence(i, charset)
    }

    @Throws(IOException::class)
    override fun readBytes(filechannel: FileChannel, i: Long, j: Int): Int {
        return source.readBytes(filechannel, i, j)
    }

    override fun skipBytes(i: Int): ByteBuf {
        return source.skipBytes(i)
    }

    override fun writeBoolean(flag: Boolean): ByteBuf {
        return source.writeBoolean(flag)
    }

    override fun writeByte(i: Int): ByteBuf {
        return source.writeByte(i)
    }

    override fun writeShort(i: Int): ByteBuf {
        return source.writeShort(i)
    }

    override fun writeShortLE(i: Int): ByteBuf {
        return source.writeShortLE(i)
    }

    override fun writeMedium(i: Int): ByteBuf {
        return source.writeMedium(i)
    }

    override fun writeMediumLE(i: Int): ByteBuf {
        return source.writeMediumLE(i)
    }

    override fun writeInt(i: Int): ByteBuf {
        return source.writeInt(i)
    }

    override fun writeIntLE(i: Int): ByteBuf {
        return source.writeIntLE(i)
    }

    override fun writeLong(i: Long): ByteBuf {
        return source.writeLong(i)
    }

    override fun writeLongLE(i: Long): ByteBuf {
        return source.writeLongLE(i)
    }

    override fun writeChar(i: Int): ByteBuf {
        return source.writeChar(i)
    }

    override fun writeFloat(f: Float): ByteBuf {
        return source.writeFloat(f)
    }

    override fun writeDouble(d0: Double): ByteBuf {
        return source.writeDouble(d0)
    }

    override fun writeBytes(bytebuf: ByteBuf): ByteBuf {
        return source.writeBytes(bytebuf)
    }

    override fun writeBytes(bytebuf: ByteBuf, i: Int): ByteBuf {
        return source.writeBytes(bytebuf, i)
    }

    override fun writeBytes(bytebuf: ByteBuf, i: Int, j: Int): ByteBuf {
        return source.writeBytes(bytebuf, i, j)
    }

    override fun writeBytes(abyte: ByteArray): ByteBuf {
        return source.writeBytes(abyte)
    }

    override fun writeBytes(abyte: ByteArray, i: Int, j: Int): ByteBuf {
        return source.writeBytes(abyte, i, j)
    }

    override fun writeBytes(bytebuffer: ByteBuffer): ByteBuf {
        return source.writeBytes(bytebuffer)
    }

    @Throws(IOException::class)
    override fun writeBytes(inputstream: InputStream, i: Int): Int {
        return source.writeBytes(inputstream, i)
    }

    @Throws(IOException::class)
    override fun writeBytes(scatteringbytechannel: ScatteringByteChannel, i: Int): Int {
        return source.writeBytes(scatteringbytechannel, i)
    }

    @Throws(IOException::class)
    override fun writeBytes(filechannel: FileChannel, i: Long, j: Int): Int {
        return source.writeBytes(filechannel, i, j)
    }

    override fun writeZero(i: Int): ByteBuf {
        return source.writeZero(i)
    }

    override fun writeCharSequence(charsequence: CharSequence, charset: Charset): Int {
        return source.writeCharSequence(charsequence, charset)
    }

    override fun indexOf(i: Int, j: Int, b0: Byte): Int {
        return source.indexOf(i, j, b0)
    }

    override fun bytesBefore(b0: Byte): Int {
        return source.bytesBefore(b0)
    }

    override fun bytesBefore(i: Int, b0: Byte): Int {
        return source.bytesBefore(i, b0)
    }

    override fun bytesBefore(i: Int, j: Int, b0: Byte): Int {
        return source.bytesBefore(i, j, b0)
    }

    override fun forEachByte(byteprocessor: ByteProcessor): Int {
        return source.forEachByte(byteprocessor)
    }

    override fun forEachByte(i: Int, j: Int, byteprocessor: ByteProcessor): Int {
        return source.forEachByte(i, j, byteprocessor)
    }

    override fun forEachByteDesc(byteprocessor: ByteProcessor): Int {
        return source.forEachByteDesc(byteprocessor)
    }

    override fun forEachByteDesc(i: Int, j: Int, byteprocessor: ByteProcessor): Int {
        return source.forEachByteDesc(i, j, byteprocessor)
    }

    override fun copy(): ByteBuf {
        return source.copy()
    }

    override fun copy(i: Int, j: Int): ByteBuf {
        return source.copy(i, j)
    }

    override fun slice(): ByteBuf {
        return source.slice()
    }

    override fun retainedSlice(): ByteBuf {
        return source.retainedSlice()
    }

    override fun slice(i: Int, j: Int): ByteBuf {
        return source.slice(i, j)
    }

    override fun retainedSlice(i: Int, j: Int): ByteBuf {
        return source.retainedSlice(i, j)
    }

    override fun duplicate(): ByteBuf {
        return source.duplicate()
    }

    override fun retainedDuplicate(): ByteBuf {
        return source.retainedDuplicate()
    }

    override fun nioBufferCount(): Int {
        return source.nioBufferCount()
    }

    override fun nioBuffer(): ByteBuffer {
        return source.nioBuffer()
    }

    override fun nioBuffer(i: Int, j: Int): ByteBuffer {
        return source.nioBuffer(i, j)
    }

    override fun internalNioBuffer(i: Int, j: Int): ByteBuffer {
        return source.internalNioBuffer(i, j)
    }

    override fun nioBuffers(): Array<ByteBuffer> {
        return source.nioBuffers()
    }

    override fun nioBuffers(i: Int, j: Int): Array<ByteBuffer> {
        return source.nioBuffers(i, j)
    }

    override fun hasArray(): Boolean {
        return source.hasArray()
    }

    override fun array(): ByteArray {
        return source.array()
    }

    override fun arrayOffset(): Int {
        return source.arrayOffset()
    }

    override fun hasMemoryAddress(): Boolean {
        return source.hasMemoryAddress()
    }

    override fun memoryAddress(): Long {
        return source.memoryAddress()
    }

    override fun toString(charset: Charset): String {
        return source.toString(charset)
    }

    override fun toString(i: Int, j: Int, charset: Charset): String {
        return source.toString(i, j, charset)
    }

    override fun hashCode(): Int {
        return source.hashCode()
    }

    override fun equals(`object`: Any?): Boolean {
        return this.source == `object`
    }

    override fun compareTo(bytebuf: ByteBuf): Int {
        return source.compareTo(bytebuf)
    }

    override fun toString(): String {
        return source.toString()
    }

    override fun retain(i: Int): ByteBuf {
        return source.retain(i)
    }

    override fun retain(): ByteBuf {
        return source.retain()
    }

    override fun touch(): ByteBuf {
        return source.touch()
    }

    override fun touch(`object`: Any): ByteBuf {
        return source.touch(`object`)
    }

    override fun refCnt(): Int {
        return source.refCnt()
    }

    override fun release(): Boolean {
        return source.release()
    }

    override fun release(i: Int): Boolean {
        return source.release(i)
    }

    companion object {
        fun getVarIntSize(value: Int): Int {
            for (j in 1..4) {
                if ((value and (-1 shl j * 7)) == 0) {
                    return j
                }
            }

            return 5
        }

        fun getVarLongSize(value: Long): Int {
            for (j in 1..9) {
                if ((value and (-1L shl j * 7)) == 0L) {
                    return j
                }
            }

            return 10
        }
    }
}