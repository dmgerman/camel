begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.boon
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|boon
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

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
name|InputStreamReader
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
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|List
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|DataFormatName
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
name|support
operator|.
name|ChildServiceSupport
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|boon
operator|.
name|json
operator|.
name|JsonFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|boon
operator|.
name|json
operator|.
name|ObjectMapper
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> (  * {@link DataFormat}) using<a  * href="http://richardhightower.github.io/site/Boon/">Boon</a> to marshal to  * and from JSON.  */
end_comment

begin_class
DECL|class|BoonDataFormat
specifier|public
class|class
name|BoonDataFormat
extends|extends
name|ChildServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
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
DECL|field|useList
specifier|private
name|boolean
name|useList
decl_stmt|;
DECL|method|BoonDataFormat ()
specifier|public
name|BoonDataFormat
parameter_list|()
block|{
name|this
argument_list|(
name|HashMap
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use the default Boon {@link ObjectMapper} and with a custom unmarshal      * type      *       * @param unmarshalType the custom unmarshal type      */
DECL|method|BoonDataFormat (Class<?> unmarshalType)
specifier|public
name|BoonDataFormat
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
name|unmarshalType
argument_list|,
name|JsonFactory
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use a custom unmarshal type and Boon mapper      *       * @param mapper the custom mapper      * @param unmarshalType the custom unmarshal type      */
DECL|method|BoonDataFormat (Class<?> unmarshalType, ObjectMapper mapper)
specifier|public
name|BoonDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|unmarshalType
parameter_list|,
name|ObjectMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|unmarshalType
operator|=
name|unmarshalType
expr_stmt|;
name|this
operator|.
name|objectMapper
operator|=
name|mapper
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"boon"
return|;
block|}
annotation|@
name|Override
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
name|BufferedWriter
name|writer
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|objectMapper
operator|.
name|toJson
argument_list|(
name|graph
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|BufferedReader
name|reader
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|objectMapper
operator|.
name|fromJson
argument_list|(
name|reader
argument_list|,
name|this
operator|.
name|unmarshalType
argument_list|)
decl_stmt|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
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
DECL|method|isUseList ()
specifier|public
name|boolean
name|isUseList
parameter_list|()
block|{
return|return
name|useList
return|;
block|}
DECL|method|setUseList (boolean useList)
specifier|public
name|void
name|setUseList
parameter_list|(
name|boolean
name|useList
parameter_list|)
block|{
name|this
operator|.
name|useList
operator|=
name|useList
expr_stmt|;
block|}
comment|/**      * Uses {@link java.util.List} when unmarshalling.      */
DECL|method|useList ()
specifier|public
name|void
name|useList
parameter_list|()
block|{
name|setUnmarshalType
argument_list|(
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

