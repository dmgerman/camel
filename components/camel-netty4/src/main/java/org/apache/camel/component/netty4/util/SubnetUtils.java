begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * A class that performs some subnet calculations given a network address and a subnet mask.  * @see "http://www.faqs.org/rfcs/rfc1519.html"  * This class is copied from apache common net   */
end_comment

begin_class
DECL|class|SubnetUtils
specifier|public
class|class
name|SubnetUtils
block|{
DECL|field|IP_ADDRESS
specifier|private
specifier|static
specifier|final
name|String
name|IP_ADDRESS
init|=
literal|"(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})"
decl_stmt|;
DECL|field|SLASH_FORMAT
specifier|private
specifier|static
specifier|final
name|String
name|SLASH_FORMAT
init|=
name|IP_ADDRESS
operator|+
literal|"/(\\d{1,3})"
decl_stmt|;
DECL|field|ADDRESS_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|ADDRESS_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|IP_ADDRESS
argument_list|)
decl_stmt|;
DECL|field|CIDR_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|CIDR_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|SLASH_FORMAT
argument_list|)
decl_stmt|;
DECL|field|NBITS
specifier|private
specifier|static
specifier|final
name|int
name|NBITS
init|=
literal|32
decl_stmt|;
DECL|field|netmask
specifier|private
name|int
name|netmask
decl_stmt|;
DECL|field|address
specifier|private
name|int
name|address
decl_stmt|;
DECL|field|network
specifier|private
name|int
name|network
decl_stmt|;
DECL|field|broadcast
specifier|private
name|int
name|broadcast
decl_stmt|;
comment|/** Whether the broadcast/network address are included in host count */
DECL|field|inclusiveHostCount
specifier|private
name|boolean
name|inclusiveHostCount
decl_stmt|;
comment|/**      * Constructor that takes a CIDR-notation string, e.g. "192.168.0.1/16"      * @param cidrNotation A CIDR-notation string, e.g. "192.168.0.1/16"      * @throws IllegalArgumentException if the parameter is invalid,      * i.e. does not match n.n.n.n/m where n=1-3 decimal digits, m = 1-3 decimal digits in range 1-32      */
DECL|method|SubnetUtils (String cidrNotation)
specifier|public
name|SubnetUtils
parameter_list|(
name|String
name|cidrNotation
parameter_list|)
block|{
name|calculate
argument_list|(
name|cidrNotation
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor that takes a dotted decimal address and a dotted decimal mask.      * @param address An IP address, e.g. "192.168.0.1"      * @param mask A dotted decimal netmask e.g. "255.255.0.0"      * @throws IllegalArgumentException if the address or mask is invalid,      * i.e. does not match n.n.n.n where n=1-3 decimal digits and the mask is not all zeros      */
DECL|method|SubnetUtils (String address, String mask)
specifier|public
name|SubnetUtils
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|mask
parameter_list|)
block|{
name|calculate
argument_list|(
name|toCidrNotation
argument_list|(
name|address
argument_list|,
name|mask
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if the return value of {@link SubnetInfo#getAddressCount()}      * includes the network and broadcast addresses.      * @since 2.2      * @return true if the hostcount includes the network and broadcast addresses      */
DECL|method|isInclusiveHostCount ()
specifier|public
name|boolean
name|isInclusiveHostCount
parameter_list|()
block|{
return|return
name|inclusiveHostCount
return|;
block|}
comment|/**      * Set to<code>true</code> if you want the return value of {@link SubnetInfo#getAddressCount()}      * to include the network and broadcast addresses.      * @param inclusiveHostCount true if network and broadcast addresses are to be included      * @since 2.2      */
DECL|method|setInclusiveHostCount (boolean inclusiveHostCount)
specifier|public
name|void
name|setInclusiveHostCount
parameter_list|(
name|boolean
name|inclusiveHostCount
parameter_list|)
block|{
name|this
operator|.
name|inclusiveHostCount
operator|=
name|inclusiveHostCount
expr_stmt|;
block|}
comment|/**      * Convenience container for subnet summary information.      *      */
DECL|class|SubnetInfo
specifier|public
specifier|final
class|class
name|SubnetInfo
block|{
comment|/* Mask to convert unsigned int to a long (i.e. keep 32 bits) */
DECL|field|UNSIGNED_INT_MASK
specifier|private
specifier|static
specifier|final
name|long
name|UNSIGNED_INT_MASK
init|=
literal|0x0FFFFFFFFL
decl_stmt|;
DECL|method|SubnetInfo ()
specifier|private
name|SubnetInfo
parameter_list|()
block|{          }
DECL|method|netmask ()
specifier|private
name|int
name|netmask
parameter_list|()
block|{
return|return
name|netmask
return|;
block|}
DECL|method|network ()
specifier|private
name|int
name|network
parameter_list|()
block|{
return|return
name|network
return|;
block|}
DECL|method|address ()
specifier|private
name|int
name|address
parameter_list|()
block|{
return|return
name|address
return|;
block|}
DECL|method|broadcast ()
specifier|private
name|int
name|broadcast
parameter_list|()
block|{
return|return
name|broadcast
return|;
block|}
comment|// long versions of the values (as unsigned int) which are more suitable for range checking
DECL|method|networkLong ()
specifier|private
name|long
name|networkLong
parameter_list|()
block|{
return|return
name|network
operator|&
name|UNSIGNED_INT_MASK
return|;
block|}
DECL|method|broadcastLong ()
specifier|private
name|long
name|broadcastLong
parameter_list|()
block|{
return|return
name|broadcast
operator|&
name|UNSIGNED_INT_MASK
return|;
block|}
DECL|method|low ()
specifier|private
name|int
name|low
parameter_list|()
block|{
return|return
name|isInclusiveHostCount
argument_list|()
condition|?
name|network
argument_list|()
else|:
name|broadcastLong
argument_list|()
operator|-
name|networkLong
argument_list|()
operator|>
literal|1
condition|?
name|network
argument_list|()
operator|+
literal|1
else|:
literal|0
return|;
block|}
DECL|method|high ()
specifier|private
name|int
name|high
parameter_list|()
block|{
return|return
name|isInclusiveHostCount
argument_list|()
condition|?
name|broadcast
argument_list|()
else|:
name|broadcastLong
argument_list|()
operator|-
name|networkLong
argument_list|()
operator|>
literal|1
condition|?
name|broadcast
argument_list|()
operator|-
literal|1
else|:
literal|0
return|;
block|}
comment|/**          * Returns true if the parameter<code>address</code> is in the          * range of usable endpoint addresses for this subnet. This excludes the          * network and broadcast adresses.          * @param address A dot-delimited IPv4 address, e.g. "192.168.0.1"          * @return True if in range, false otherwise          */
DECL|method|isInRange (String address)
specifier|public
name|boolean
name|isInRange
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|ADDRESS_PATTERN
operator|.
name|matcher
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|isInRange
argument_list|(
name|toInteger
argument_list|(
name|address
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|isInRange (int address)
specifier|private
name|boolean
name|isInRange
parameter_list|(
name|int
name|address
parameter_list|)
block|{
name|long
name|addLong
init|=
name|address
operator|&
name|UNSIGNED_INT_MASK
decl_stmt|;
name|long
name|lowLong
init|=
name|low
argument_list|()
operator|&
name|UNSIGNED_INT_MASK
decl_stmt|;
name|long
name|highLong
init|=
name|high
argument_list|()
operator|&
name|UNSIGNED_INT_MASK
decl_stmt|;
return|return
name|addLong
operator|>=
name|lowLong
operator|&&
name|addLong
operator|<=
name|highLong
return|;
block|}
DECL|method|getBroadcastAddress ()
specifier|public
name|String
name|getBroadcastAddress
parameter_list|()
block|{
return|return
name|format
argument_list|(
name|toArray
argument_list|(
name|broadcast
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getNetworkAddress ()
specifier|public
name|String
name|getNetworkAddress
parameter_list|()
block|{
return|return
name|format
argument_list|(
name|toArray
argument_list|(
name|network
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getNetmask ()
specifier|public
name|String
name|getNetmask
parameter_list|()
block|{
return|return
name|format
argument_list|(
name|toArray
argument_list|(
name|netmask
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getAddress ()
specifier|public
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|format
argument_list|(
name|toArray
argument_list|(
name|address
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**          * Return the low address as a dotted IP address.          * Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.          *          * @return the IP address in dotted format, may be "0.0.0.0" if there is no valid address          */
DECL|method|getLowAddress ()
specifier|public
name|String
name|getLowAddress
parameter_list|()
block|{
return|return
name|format
argument_list|(
name|toArray
argument_list|(
name|low
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**          * Return the high address as a dotted IP address.          * Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.          *          * @return the IP address in dotted format, may be "0.0.0.0" if there is no valid address          */
DECL|method|getHighAddress ()
specifier|public
name|String
name|getHighAddress
parameter_list|()
block|{
return|return
name|format
argument_list|(
name|toArray
argument_list|(
name|high
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**          * Get the count of available addresses.          * Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.          * @return the count of addresses, may be zero.          * @throws RuntimeException if the correct count is greater than {@code Integer.MAX_VALUE}          * @deprecated use {@link #getAddressCountLong()} instead          */
annotation|@
name|Deprecated
DECL|method|getAddressCount ()
specifier|public
name|int
name|getAddressCount
parameter_list|()
block|{
name|long
name|countLong
init|=
name|getAddressCountLong
argument_list|()
decl_stmt|;
if|if
condition|(
name|countLong
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Count is larger than an integer: "
operator|+
name|countLong
argument_list|)
throw|;
block|}
comment|// N.B. cannot be negative
return|return
operator|(
name|int
operator|)
name|countLong
return|;
block|}
comment|/**          * Get the count of available addresses.          * Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.          * @return the count of addresses, may be zero.          */
DECL|method|getAddressCountLong ()
specifier|public
name|long
name|getAddressCountLong
parameter_list|()
block|{
name|long
name|b
init|=
name|broadcastLong
argument_list|()
decl_stmt|;
name|long
name|n
init|=
name|networkLong
argument_list|()
decl_stmt|;
name|long
name|count
init|=
name|b
operator|-
name|n
operator|+
operator|(
name|isInclusiveHostCount
argument_list|()
condition|?
literal|1
else|:
operator|-
literal|1
operator|)
decl_stmt|;
return|return
name|count
operator|<
literal|0
condition|?
literal|0
else|:
name|count
return|;
block|}
DECL|method|asInteger (String address)
specifier|public
name|int
name|asInteger
parameter_list|(
name|String
name|address
parameter_list|)
block|{
return|return
name|toInteger
argument_list|(
name|address
argument_list|)
return|;
block|}
DECL|method|getCidrSignature ()
specifier|public
name|String
name|getCidrSignature
parameter_list|()
block|{
return|return
name|toCidrNotation
argument_list|(
name|format
argument_list|(
name|toArray
argument_list|(
name|address
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|format
argument_list|(
name|toArray
argument_list|(
name|netmask
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getAllAddresses ()
specifier|public
name|String
index|[]
name|getAllAddresses
parameter_list|()
block|{
name|int
name|ct
init|=
name|getAddressCount
argument_list|()
decl_stmt|;
name|String
index|[]
name|addresses
init|=
operator|new
name|String
index|[
name|ct
index|]
decl_stmt|;
if|if
condition|(
name|ct
operator|==
literal|0
condition|)
block|{
return|return
name|addresses
return|;
block|}
name|int
name|j
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|add
init|=
name|low
argument_list|()
init|;
name|add
operator|<=
name|high
argument_list|()
condition|;
operator|++
name|add
control|)
block|{
name|addresses
index|[
name|j
index|]
operator|=
name|format
argument_list|(
name|toArray
argument_list|(
name|add
argument_list|)
argument_list|)
expr_stmt|;
operator|++
name|j
expr_stmt|;
block|}
return|return
name|addresses
return|;
block|}
comment|/**          * {@inheritDoc}          * @since 2.2          */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"CIDR Signature:\t["
argument_list|)
operator|.
name|append
argument_list|(
name|getCidrSignature
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
operator|.
name|append
argument_list|(
literal|" Netmask: ["
argument_list|)
operator|.
name|append
argument_list|(
name|getNetmask
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Network:\t["
argument_list|)
operator|.
name|append
argument_list|(
name|getNetworkAddress
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Broadcast:\t["
argument_list|)
operator|.
name|append
argument_list|(
name|getBroadcastAddress
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"First Address:\t["
argument_list|)
operator|.
name|append
argument_list|(
name|getLowAddress
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"Last Address:\t["
argument_list|)
operator|.
name|append
argument_list|(
name|getHighAddress
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]\n"
argument_list|)
operator|.
name|append
argument_list|(
literal|"# Addresses:\t["
argument_list|)
operator|.
name|append
argument_list|(
name|getAddressCount
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]\n"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**      * Return a {@link SubnetInfo} instance that contains subnet-specific statistics      * @return new instance      */
DECL|method|getInfo ()
specifier|public
specifier|final
name|SubnetInfo
name|getInfo
parameter_list|()
block|{
return|return
operator|new
name|SubnetInfo
argument_list|()
return|;
block|}
comment|/*      * Initialize the internal fields from the supplied CIDR mask      */
DECL|method|calculate (String mask)
specifier|private
name|void
name|calculate
parameter_list|(
name|String
name|mask
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|CIDR_PATTERN
operator|.
name|matcher
argument_list|(
name|mask
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|address
operator|=
name|matchAddress
argument_list|(
name|matcher
argument_list|)
expr_stmt|;
comment|/* Create a binary netmask from the number of bits specification /x */
name|int
name|cidrPart
init|=
name|rangeCheck
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|,
literal|0
argument_list|,
name|NBITS
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|cidrPart
condition|;
operator|++
name|j
control|)
block|{
name|netmask
operator||=
literal|1
operator|<<
literal|31
operator|-
name|j
expr_stmt|;
block|}
comment|/* Calculate base network address */
name|network
operator|=
name|address
operator|&
name|netmask
expr_stmt|;
comment|/* Calculate broadcast address */
name|broadcast
operator|=
name|network
operator||
operator|~
name|netmask
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not parse ["
operator|+
name|mask
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
comment|/*      * Convert a dotted decimal format address to a packed integer format      */
DECL|method|toInteger (String address)
specifier|private
name|int
name|toInteger
parameter_list|(
name|String
name|address
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|ADDRESS_PATTERN
operator|.
name|matcher
argument_list|(
name|address
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|matchAddress
argument_list|(
name|matcher
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not parse ["
operator|+
name|address
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
comment|/*      * Convenience method to extract the components of a dotted decimal address and      * pack into an integer using a regex match      */
DECL|method|matchAddress (Matcher matcher)
specifier|private
name|int
name|matchAddress
parameter_list|(
name|Matcher
name|matcher
parameter_list|)
block|{
name|int
name|addr
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|4
condition|;
operator|++
name|i
control|)
block|{
name|int
name|n
init|=
name|rangeCheck
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|,
literal|0
argument_list|,
literal|255
argument_list|)
decl_stmt|;
name|addr
operator||=
operator|(
name|n
operator|&
literal|0xff
operator|)
operator|<<
literal|8
operator|*
operator|(
literal|4
operator|-
name|i
operator|)
expr_stmt|;
block|}
return|return
name|addr
return|;
block|}
comment|/*      * Convert a packed integer address into a 4-element array      */
DECL|method|toArray (int val)
specifier|private
name|int
index|[]
name|toArray
parameter_list|(
name|int
name|val
parameter_list|)
block|{
name|int
name|ret
index|[]
init|=
operator|new
name|int
index|[
literal|4
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|3
init|;
name|j
operator|>=
literal|0
condition|;
operator|--
name|j
control|)
block|{
name|ret
index|[
name|j
index|]
operator||=
operator|(
name|val
operator|>>>
literal|8
operator|*
operator|(
literal|3
operator|-
name|j
operator|)
operator|)
operator|&
literal|0xff
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
comment|/*      * Convert a 4-element array into dotted decimal format      */
DECL|method|format (int[] octets)
specifier|private
name|String
name|format
parameter_list|(
name|int
index|[]
name|octets
parameter_list|)
block|{
name|StringBuilder
name|str
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|octets
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|str
operator|.
name|append
argument_list|(
name|octets
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|!=
name|octets
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|str
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|str
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/*      * Convenience function to check integer boundaries.      * Checks if a value x is in the range [begin,end].      * Returns x if it is in range, throws an exception otherwise.      */
DECL|method|rangeCheck (int value, int begin, int end)
specifier|private
name|int
name|rangeCheck
parameter_list|(
name|int
name|value
parameter_list|,
name|int
name|begin
parameter_list|,
name|int
name|end
parameter_list|)
block|{
if|if
condition|(
name|value
operator|>=
name|begin
operator|&&
name|value
operator|<=
name|end
condition|)
block|{
comment|// (begin,end]
return|return
name|value
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value ["
operator|+
name|value
operator|+
literal|"] not in range ["
operator|+
name|begin
operator|+
literal|","
operator|+
name|end
operator|+
literal|"]"
argument_list|)
throw|;
block|}
comment|/*      * Count the number of 1-bits in a 32-bit integer using a divide-and-conquer strategy      * see Hacker's Delight section 5.1      */
DECL|method|pop (int x)
name|int
name|pop
parameter_list|(
name|int
name|x
parameter_list|)
block|{
name|x
operator|=
name|x
operator|-
operator|(
operator|(
name|x
operator|>>>
literal|1
operator|)
operator|&
literal|0x55555555
operator|)
expr_stmt|;
name|x
operator|=
operator|(
name|x
operator|&
literal|0x33333333
operator|)
operator|+
operator|(
operator|(
name|x
operator|>>>
literal|2
operator|)
operator|&
literal|0x33333333
operator|)
expr_stmt|;
name|x
operator|=
operator|(
name|x
operator|+
operator|(
name|x
operator|>>>
literal|4
operator|)
operator|)
operator|&
literal|0x0F0F0F0F
expr_stmt|;
name|x
operator|=
name|x
operator|+
operator|(
name|x
operator|>>>
literal|8
operator|)
expr_stmt|;
name|x
operator|=
name|x
operator|+
operator|(
name|x
operator|>>>
literal|16
operator|)
expr_stmt|;
return|return
name|x
operator|&
literal|0x0000003F
return|;
block|}
comment|/* Convert two dotted decimal addresses to a single xxx.xxx.xxx.xxx/yy format      * by counting the 1-bit population in the mask address. (It may be better to count      * NBITS-#trailing zeroes for this case)      */
DECL|method|toCidrNotation (String addr, String mask)
specifier|private
name|String
name|toCidrNotation
parameter_list|(
name|String
name|addr
parameter_list|,
name|String
name|mask
parameter_list|)
block|{
return|return
name|addr
operator|+
literal|"/"
operator|+
name|pop
argument_list|(
name|toInteger
argument_list|(
name|mask
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

