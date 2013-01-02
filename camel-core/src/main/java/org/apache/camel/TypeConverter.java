begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A pluggable strategy to be able to convert objects<a  * href="http://camel.apache.org/type-converter.html">to different  * types</a> such as to and from String, InputStream/OutputStream,  * Reader/Writer, Document, byte[], ByteBuffer etc  *   * @version   */
end_comment

begin_interface
DECL|interface|TypeConverter
specifier|public
interface|interface
name|TypeConverter
block|{
comment|/**      * Converts the value to the specified type      *      * @param type the requested type      * @param value the value to be converted      * @return the converted value, or<tt>null</tt> if not possible to convert      * @throws TypeConversionException is thrown if error during type conversion      */
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
throws|throws
name|TypeConversionException
function_decl|;
comment|/**      * Converts the value to the specified type in the context of an exchange      *<p/>      * Used when conversion requires extra information from the current      * exchange (such as encoding).      *      * @param type the requested type      * @param exchange the current exchange      * @param value the value to be converted      * @return the converted value, or<tt>null</tt> if not possible to convert      * @throws TypeConversionException is thrown if error during type conversion      */
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
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
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|TypeConversionException
function_decl|;
comment|/**      * Converts the value to the specified type      *      * @param type the requested type      * @param value the value to be converted      * @return the converted value, is never<tt>null</tt>      * @throws TypeConversionException is thrown if error during type conversion      * @throws NoTypeConversionAvailableException if no type converters exists to convert to the given type      */
DECL|method|mandatoryConvertTo (Class<T> type, Object value)
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
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
throws|throws
name|TypeConversionException
throws|,
name|NoTypeConversionAvailableException
function_decl|;
comment|/**      * Converts the value to the specified type in the context of an exchange      *<p/>      * Used when conversion requires extra information from the current      * exchange (such as encoding).      *      * @param type the requested type      * @param exchange the current exchange      * @param value the value to be converted      * @return the converted value, is never<tt>null</tt>      * @throws TypeConversionException is thrown if error during type conversion      * @throws NoTypeConversionAvailableException if no type converters exists to convert to the given type      */
DECL|method|mandatoryConvertTo (Class<T> type, Exchange exchange, Object value)
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|TypeConversionException
throws|,
name|NoTypeConversionAvailableException
function_decl|;
comment|/**      * Tries to convert the value to the specified type,      * returning<tt>null</tt> if not possible to convert.      *<p/>      * This method will<b>not</b> throw an exception if an exception occurred during conversion.      *      * @param type the requested type      * @param value the value to be converted      * @return the converted value, or<tt>null</tt> if not possible to convert      */
DECL|method|tryConvertTo (Class<T> type, Object value)
parameter_list|<
name|T
parameter_list|>
name|T
name|tryConvertTo
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
comment|/**      * Tries to convert the value to the specified type in the context of an exchange,      * returning<tt>null</tt> if not possible to convert.      *<p/>      * This method will<b>not</b> throw an exception if an exception occurred during conversion.      * Converts the value to the specified type in the context of an exchange      *<p/>      * Used when conversion requires extra information from the current      * exchange (such as encoding).      *      * @param type the requested type      * @param exchange the current exchange      * @param value the value to be converted      * @return the converted value, or<tt>null</tt> if not possible to convert      */
DECL|method|tryConvertTo (Class<T> type, Exchange exchange, Object value)
parameter_list|<
name|T
parameter_list|>
name|T
name|tryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

