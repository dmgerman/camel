begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.batch.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|batch
operator|.
name|support
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
name|ConsumerTemplate
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|item
operator|.
name|ItemReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_class
DECL|class|CamelItemReader
specifier|public
class|class
name|CamelItemReader
parameter_list|<
name|I
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|ItemReader
argument_list|<
name|I
argument_list|>
implements|,
name|InitializingBean
implements|,
name|CamelContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelItemReader
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|consumerTemplate
specifier|private
specifier|final
name|ConsumerTemplate
name|consumerTemplate
decl_stmt|;
DECL|field|endpointUri
specifier|private
specifier|final
name|String
name|endpointUri
decl_stmt|;
DECL|method|CamelItemReader (ConsumerTemplate consumerTemplate, String endpointUri)
specifier|public
name|CamelItemReader
parameter_list|(
name|ConsumerTemplate
name|consumerTemplate
parameter_list|,
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|consumerTemplate
operator|=
name|consumerTemplate
expr_stmt|;
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
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
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// register this as service so we get lifecycle callback when Camel is starting/stopping
name|camelContext
operator|.
name|addService
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|read ()
specifier|public
name|I
name|read
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"reading new item..."
argument_list|)
expr_stmt|;
name|I
name|item
init|=
operator|(
name|I
operator|)
name|consumerTemplate
operator|.
name|receiveBody
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"read item [{}]"
argument_list|,
name|item
argument_list|)
expr_stmt|;
return|return
name|item
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
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumerTemplate
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
name|consumerTemplate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

