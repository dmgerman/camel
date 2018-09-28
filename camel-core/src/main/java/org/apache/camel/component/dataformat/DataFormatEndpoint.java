begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataformat
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|impl
operator|.
name|DefaultAsyncProducer
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
name|impl
operator|.
name|DefaultEndpoint
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
name|processor
operator|.
name|MarshalProcessor
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
name|processor
operator|.
name|UnmarshalProcessor
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
name|Metadata
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
name|UriEndpoint
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
name|UriPath
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * The dataformat component is used for working with Data Formats as if it was a regular Component supporting Endpoints and URIs.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"dataformat"
argument_list|,
name|title
operator|=
literal|"Data Format"
argument_list|,
name|syntax
operator|=
literal|"dataformat:name:operation"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"core,transformation"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|DataFormatEndpoint
specifier|public
class|class
name|DataFormatEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|processor
specifier|private
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|dataFormat
specifier|private
name|DataFormat
name|dataFormat
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of data format"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"marshal,unmarshal"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
DECL|method|DataFormatEndpoint ()
specifier|public
name|DataFormatEndpoint
parameter_list|()
block|{     }
DECL|method|DataFormatEndpoint (String endpointUri, Component component, DataFormat dataFormat)
specifier|public
name|DataFormatEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getDataFormat ()
specifier|public
name|DataFormat
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
DECL|method|setDataFormat (DataFormat dataFormat)
specifier|public
name|void
name|setDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * Operation to use either marshal or unmarshal      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultAsyncProducer
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DataFormatProducer["
operator|+
name|dataFormat
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Cannot consume from data format"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
literal|true
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
if|if
condition|(
name|dataFormat
operator|==
literal|null
operator|&&
name|name
operator|!=
literal|null
condition|)
block|{
name|dataFormat
operator|=
name|getCamelContext
argument_list|()
operator|.
name|resolveDataFormat
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|operation
operator|.
name|equals
argument_list|(
literal|"marshal"
argument_list|)
condition|)
block|{
name|MarshalProcessor
name|marshal
init|=
operator|new
name|MarshalProcessor
argument_list|(
name|dataFormat
argument_list|)
decl_stmt|;
name|marshal
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|processor
operator|=
name|marshal
expr_stmt|;
block|}
else|else
block|{
name|UnmarshalProcessor
name|unmarshal
init|=
operator|new
name|UnmarshalProcessor
argument_list|(
name|dataFormat
argument_list|)
decl_stmt|;
name|unmarshal
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|processor
operator|=
name|unmarshal
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|dataFormat
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|processor
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

