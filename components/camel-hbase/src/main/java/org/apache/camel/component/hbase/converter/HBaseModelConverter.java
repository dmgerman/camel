begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|util
operator|.
name|Bytes
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|HBaseModelConverter
specifier|public
specifier|final
class|class
name|HBaseModelConverter
block|{
DECL|method|HBaseModelConverter ()
specifier|private
name|HBaseModelConverter
parameter_list|()
block|{
comment|//Utility Class
block|}
annotation|@
name|Converter
DECL|method|booleanToBytes (Boolean bool)
specifier|public
specifier|static
name|byte
index|[]
name|booleanToBytes
parameter_list|(
name|Boolean
name|bool
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBytes
argument_list|(
name|bool
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|bytesToBoolean (byte[] bytes)
specifier|public
specifier|static
name|Boolean
name|bytesToBoolean
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBoolean
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|shortToBytes (Short num)
specifier|public
specifier|static
name|byte
index|[]
name|shortToBytes
parameter_list|(
name|Short
name|num
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBytes
argument_list|(
name|num
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|bytesToShort (byte[] bytes)
specifier|public
specifier|static
name|Short
name|bytesToShort
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toShort
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|integerToBytes (Integer num)
specifier|public
specifier|static
name|byte
index|[]
name|integerToBytes
parameter_list|(
name|Integer
name|num
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBytes
argument_list|(
name|num
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|bytesToInteger (byte[] bytes)
specifier|public
specifier|static
name|Integer
name|bytesToInteger
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toInt
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|longToBytes (Long num)
specifier|public
specifier|static
name|byte
index|[]
name|longToBytes
parameter_list|(
name|Long
name|num
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBytes
argument_list|(
name|num
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|bytesToLong (byte[] bytes)
specifier|public
specifier|static
name|Long
name|bytesToLong
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toLong
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|doubleToBytes (Double num)
specifier|public
specifier|static
name|byte
index|[]
name|doubleToBytes
parameter_list|(
name|Double
name|num
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBytes
argument_list|(
name|num
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|bytesToDouble (byte[] bytes)
specifier|public
specifier|static
name|Double
name|bytesToDouble
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toDouble
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|floatToBytes (Float num)
specifier|public
specifier|static
name|byte
index|[]
name|floatToBytes
parameter_list|(
name|Float
name|num
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBytes
argument_list|(
name|num
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|bytesToFloat (byte[] bytes)
specifier|public
specifier|static
name|Float
name|bytesToFloat
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toFloat
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|stringToBytes (String str)
specifier|public
specifier|static
name|byte
index|[]
name|stringToBytes
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toBytes
argument_list|(
name|str
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|bytesToString (byte[] bytes)
specifier|public
specifier|static
name|String
name|bytesToString
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Bytes
operator|.
name|toString
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

