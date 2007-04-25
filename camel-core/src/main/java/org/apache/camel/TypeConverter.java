begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * A pluggable strategy to be able to convert objects  *<a href="http://activemq.apache.org/camel/type-converter.html">to different types</a>  * such as to and from String, InputStream/OutputStream, Reader/Writer, Document, byte[], ByteBuffer etc  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|TypeConverter
specifier|public
interface|interface
name|TypeConverter
block|{
comment|/**      * Converts the value to the specified type      * @param type the requested type      * @param value the value to be converted      * @return the converted value or null if it can not be converted      */
DECL|method|convertTo (Class<T> type, Object value)
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

