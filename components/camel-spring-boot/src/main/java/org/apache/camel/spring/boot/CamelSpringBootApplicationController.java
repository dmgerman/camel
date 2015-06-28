begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|ProducerTemplate
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
name|main
operator|.
name|MainSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_class
DECL|class|CamelSpringBootApplicationController
specifier|public
class|class
name|CamelSpringBootApplicationController
block|{
DECL|field|mainSupport
specifier|private
specifier|final
name|MainSupport
name|mainSupport
decl_stmt|;
DECL|method|CamelSpringBootApplicationController (final ApplicationContext applicationContext, final CamelContext camelContext)
specifier|public
name|CamelSpringBootApplicationController
parameter_list|(
specifier|final
name|ApplicationContext
name|applicationContext
parameter_list|,
specifier|final
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|mainSupport
operator|=
operator|new
name|MainSupport
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|ProducerTemplate
name|findOrCreateCamelTemplate
parameter_list|()
block|{
return|return
name|applicationContext
operator|.
name|getBean
argument_list|(
name|ProducerTemplate
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|CamelContext
argument_list|>
name|getCamelContextMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"camelContext"
argument_list|,
name|camelContext
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|blockMainThread ()
specifier|public
name|void
name|blockMainThread
parameter_list|()
block|{
try|try
block|{
name|mainSupport
operator|.
name|enableHangupSupport
argument_list|()
expr_stmt|;
name|mainSupport
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|PreDestroy
DECL|method|destroy ()
specifier|private
name|void
name|destroy
parameter_list|()
block|{
name|mainSupport
operator|.
name|completed
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

