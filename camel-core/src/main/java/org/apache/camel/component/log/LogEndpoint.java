begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|ProcessorEndpoint
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
name|UriParam
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
comment|/**  * Logger endpoint.  */
end_comment

begin_class
DECL|class|LogEndpoint
specifier|public
class|class
name|LogEndpoint
extends|extends
name|ProcessorEndpoint
block|{
DECL|field|logger
specifier|private
specifier|volatile
name|Processor
name|logger
decl_stmt|;
annotation|@
name|UriParam
DECL|field|level
specifier|private
name|String
name|level
decl_stmt|;
annotation|@
name|UriParam
DECL|field|marker
specifier|private
name|String
name|marker
decl_stmt|;
annotation|@
name|UriParam
DECL|field|groupSize
specifier|private
name|Integer
name|groupSize
decl_stmt|;
annotation|@
name|UriParam
DECL|field|groupInterval
specifier|private
name|Long
name|groupInterval
decl_stmt|;
annotation|@
name|UriParam
DECL|field|groupActiveOnly
specifier|private
name|Boolean
name|groupActiveOnly
decl_stmt|;
annotation|@
name|UriParam
DECL|field|groupDelay
specifier|private
name|Long
name|groupDelay
decl_stmt|;
DECL|method|LogEndpoint ()
specifier|public
name|LogEndpoint
parameter_list|()
block|{     }
DECL|method|LogEndpoint (String endpointUri, Component component)
specifier|public
name|LogEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|LogEndpoint (String endpointUri, Component component, Processor logger)
specifier|public
name|LogEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Processor
name|logger
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setLogger
argument_list|(
name|logger
argument_list|)
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|logger
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
name|logger
argument_list|)
expr_stmt|;
block|}
DECL|method|setLogger (Processor logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|Processor
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
comment|// the logger is the processor
name|setProcessor
argument_list|(
name|this
operator|.
name|logger
argument_list|)
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|public
name|Processor
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
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
name|LogProducer
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|logger
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"log:"
operator|+
name|logger
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getLevel ()
specifier|public
name|String
name|getLevel
parameter_list|()
block|{
return|return
name|level
return|;
block|}
DECL|method|setLevel (String level)
specifier|public
name|void
name|setLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
block|}
DECL|method|getMarker ()
specifier|public
name|String
name|getMarker
parameter_list|()
block|{
return|return
name|marker
return|;
block|}
DECL|method|setMarker (String marker)
specifier|public
name|void
name|setMarker
parameter_list|(
name|String
name|marker
parameter_list|)
block|{
name|this
operator|.
name|marker
operator|=
name|marker
expr_stmt|;
block|}
DECL|method|getGroupSize ()
specifier|public
name|Integer
name|getGroupSize
parameter_list|()
block|{
return|return
name|groupSize
return|;
block|}
DECL|method|setGroupSize (Integer groupSize)
specifier|public
name|void
name|setGroupSize
parameter_list|(
name|Integer
name|groupSize
parameter_list|)
block|{
name|this
operator|.
name|groupSize
operator|=
name|groupSize
expr_stmt|;
block|}
DECL|method|getGroupInterval ()
specifier|public
name|Long
name|getGroupInterval
parameter_list|()
block|{
return|return
name|groupInterval
return|;
block|}
DECL|method|setGroupInterval (Long groupInterval)
specifier|public
name|void
name|setGroupInterval
parameter_list|(
name|Long
name|groupInterval
parameter_list|)
block|{
name|this
operator|.
name|groupInterval
operator|=
name|groupInterval
expr_stmt|;
block|}
DECL|method|getGroupActiveOnly ()
specifier|public
name|Boolean
name|getGroupActiveOnly
parameter_list|()
block|{
return|return
name|groupActiveOnly
return|;
block|}
DECL|method|setGroupActiveOnly (Boolean groupActiveOnly)
specifier|public
name|void
name|setGroupActiveOnly
parameter_list|(
name|Boolean
name|groupActiveOnly
parameter_list|)
block|{
name|this
operator|.
name|groupActiveOnly
operator|=
name|groupActiveOnly
expr_stmt|;
block|}
DECL|method|getGroupDelay ()
specifier|public
name|Long
name|getGroupDelay
parameter_list|()
block|{
return|return
name|groupDelay
return|;
block|}
DECL|method|setGroupDelay (Long groupDelay)
specifier|public
name|void
name|setGroupDelay
parameter_list|(
name|Long
name|groupDelay
parameter_list|)
block|{
name|this
operator|.
name|groupDelay
operator|=
name|groupDelay
expr_stmt|;
block|}
block|}
end_class

end_unit

