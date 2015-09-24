begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|CamelContext
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
name|CamelContextAware
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
name|Message
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
name|Traceable
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
name|converter
operator|.
name|stream
operator|.
name|CachedOutputStream
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
name|IdAware
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
name|ServiceSupport
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
name|AsyncProcessorHelper
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
name|ObjectHelper
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
comment|/**  * Marshals the body of the incoming message using the given  *<a href="http://camel.apache.org/data-format.html">data format</a>  *  * @version  */
end_comment

begin_class
DECL|class|MarshalProcessor
specifier|public
class|class
name|MarshalProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
implements|,
name|CamelContextAware
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|dataFormat
specifier|private
specifier|final
name|DataFormat
name|dataFormat
decl_stmt|;
DECL|method|MarshalProcessor (DataFormat dataFormat)
specifier|public
name|MarshalProcessor
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|dataFormat
argument_list|,
literal|"dataFormat"
argument_list|)
expr_stmt|;
comment|// if stream caching is enabled then use that so we can stream accordingly
comment|// for example to overflow to disk for big streams
name|CachedOutputStream
name|cos
decl_stmt|;
name|ByteArrayOutputStream
name|os
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
name|cos
operator|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|os
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|cos
operator|=
literal|null
expr_stmt|;
name|os
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
block|}
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|body
init|=
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
comment|// lets setup the out message before we invoke the dataFormat
comment|// so that it can mutate it if necessary
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|in
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|cos
operator|!=
literal|null
condition|)
block|{
name|dataFormat
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|,
name|cos
argument_list|)
expr_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|cos
operator|.
name|newStreamCache
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataFormat
operator|.
name|marshal
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
name|os
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// remove OUT message, as an exception occurred
name|exchange
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Marshal["
operator|+
name|dataFormat
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"marshal["
operator|+
name|dataFormat
operator|+
literal|"]"
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
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
comment|// inject CamelContext on data format
if|if
condition|(
name|dataFormat
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|dataFormat
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
comment|// add dataFormat as service which will also start the service
comment|// (false => we and handling the lifecycle of the dataFormat)
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|dataFormat
argument_list|,
literal|false
argument_list|)
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
name|dataFormat
argument_list|)
expr_stmt|;
name|getCamelContext
argument_list|()
operator|.
name|removeService
argument_list|(
name|dataFormat
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

