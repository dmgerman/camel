begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_comment
comment|/**  * Allows {@link org.apache.camel.Message} to store a {@link DataType} which  * represents the data type of the Message. Sometimes message content is marshaled  * into {@code String}, {@code InputStream} or etc, and the data type structure is  * not available until it's unmarshaled into Java object. The {@link DataType} stored  * in a DataTypeAware message carries that missing data type information even if it's  * marshaled, and whatever the Java class of the body is. This type information is used  * to detect required {@link Transformer} and {@link Validator}.  *<p/>  * Data type are automatic turned on if one ore more routes has been explicit configured with input and output types.  * Otherwise data type is default off.  *  * @see DataType  * @see Transformer  * @see Validator  */
end_comment

begin_interface
DECL|interface|DataTypeAware
specifier|public
interface|interface
name|DataTypeAware
block|{
comment|/**      * Set the data type of the message.      *      * @param type data type      */
DECL|method|setDataType (DataType type)
name|void
name|setDataType
parameter_list|(
name|DataType
name|type
parameter_list|)
function_decl|;
comment|/**      * Get the data type of the message.      *      * @return data type      */
DECL|method|getDataType ()
name|DataType
name|getDataType
parameter_list|()
function_decl|;
comment|/**      * Whether any data type has been configured      */
DECL|method|hasDataType ()
name|boolean
name|hasDataType
parameter_list|()
function_decl|;
comment|/**      * Set the message body with data type.      *      * @param body message body      * @param type data type      */
DECL|method|setBody (Object body, DataType type)
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|DataType
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

