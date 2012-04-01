begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jackson
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jackson
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|DataFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|map
operator|.
name|ObjectMapper
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})  * using<a href="http://jackson.codehaus.org/">Jackson</a> to marshal to and from JSON.  */
end_comment

begin_class
DECL|class|JacksonDataFormat
specifier|public
class|class
name|JacksonDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|objectMapper
specifier|private
specifier|final
name|ObjectMapper
name|objectMapper
decl_stmt|;
DECL|field|unmarshalType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
decl_stmt|;
DECL|field|jsonView
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
decl_stmt|;
comment|/**      * Use the default Jackson {@link ObjectMapper} and {@link Map}      */
DECL|method|JacksonDataFormat ()
specifier|public
name|JacksonDataFormat
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|ObjectMapper
argument_list|()
argument_list|,
name|HashMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use the default Jackson {@link ObjectMapper} and with a custom      * unmarshal type      *      * @param unmarshalType the custom unmarshal type      */
DECL|method|JacksonDataFormat (Class<?> unmarshalType)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|ObjectMapper
argument_list|()
argument_list|,
name|unmarshalType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use the default Jackson {@link ObjectMapper} and with a custom      * unmarshal type and JSON view      *      * @param unmarshalType the custom unmarshal type      * @param jsonView marker class to specifiy properties to be included during marshalling.      *                 See also http://wiki.fasterxml.com/JacksonJsonViews      */
DECL|method|JacksonDataFormat (Class<?> unmarshalType, Class<?> jsonView)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|ObjectMapper
argument_list|()
argument_list|,
name|unmarshalType
argument_list|,
name|jsonView
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use a custom Jackson mapper and and unmarshal type      *      * @param mapper        the custom mapper      * @param unmarshalType the custom unmarshal type      */
DECL|method|JacksonDataFormat (ObjectMapper mapper, Class<?> unmarshalType)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
argument_list|(
name|mapper
argument_list|,
name|unmarshalType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use a custom Jackson mapper, unmarshal type and JSON view      *      * @param mapper        the custom mapper      * @param unmarshalType the custom unmarshal type      * @param jsonView marker class to specifiy properties to be included during marshalling.      *                 See also http://wiki.fasterxml.com/JacksonJsonViews      */
DECL|method|JacksonDataFormat (ObjectMapper mapper, Class<?> unmarshalType, Class<?> jsonView)
specifier|public
name|JacksonDataFormat
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|this
operator|.
name|objectMapper
operator|=
name|mapper
expr_stmt|;
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
name|this
operator|.
name|jsonView
operator|=
name|jsonView
expr_stmt|;
block|}
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|objectMapper
operator|.
name|writerWithView
argument_list|(
name|jsonView
argument_list|)
operator|.
name|writeValue
argument_list|(
name|stream
argument_list|,
name|graph
argument_list|)
expr_stmt|;
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|objectMapper
operator|.
name|readValue
argument_list|(
name|stream
argument_list|,
name|this
operator|.
name|unmarshalType
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getUnmarshalType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getUnmarshalType
parameter_list|()
block|{
return|return
name|this
operator|.
name|unmarshalType
return|;
block|}
DECL|method|setUnmarshalType (Class<?> unmarshalType)
specifier|public
name|void
name|setUnmarshalType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|)
block|{
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
block|}
DECL|method|getJsonView ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getJsonView
parameter_list|()
block|{
return|return
name|jsonView
return|;
block|}
DECL|method|setJsonView (Class<?> jsonView)
specifier|public
name|void
name|setJsonView
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|jsonView
parameter_list|)
block|{
name|this
operator|.
name|jsonView
operator|=
name|jsonView
expr_stmt|;
block|}
DECL|method|getObjectMapper ()
specifier|public
name|ObjectMapper
name|getObjectMapper
parameter_list|()
block|{
return|return
name|this
operator|.
name|objectMapper
return|;
block|}
block|}
end_class

end_unit

