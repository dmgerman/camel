begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
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
name|impl
operator|.
name|DefaultCamelContext
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
name|Injector
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
name|ComponentResolver
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
name|spring
operator|.
name|spi
operator|.
name|SpringComponentResolver
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
name|spring
operator|.
name|spi
operator|.
name|SpringInjector
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
name|DisposableBean
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
name|BeansException
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
name|ApplicationContextAware
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
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|support
operator|.
name|AbstractRefreshableApplicationContext
import|;
end_import

begin_comment
comment|/**  * A Spring aware implementation of {@link CamelContext} which will automatically register itself with Springs lifecycle  * methods  plus allows spring to be used to customize a any  *<a href="http://activemq.apache.org/camel/type-converter.html">Type Converters</a> as well as supporting accessing components  * and beans via the Spring {@link ApplicationContext}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SpringCamelContext
specifier|public
class|class
name|SpringCamelContext
extends|extends
name|DefaultCamelContext
implements|implements
name|InitializingBean
implements|,
name|DisposableBean
implements|,
name|ApplicationContextAware
block|{
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|SpringCamelContext ()
specifier|public
name|SpringCamelContext
parameter_list|()
block|{     }
DECL|method|SpringCamelContext (ApplicationContext applicationContext)
specifier|public
name|SpringCamelContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
DECL|method|springCamelContext (ApplicationContext applicationContext)
specifier|public
specifier|static
name|SpringCamelContext
name|springCamelContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// lets try and look up a configured camel context in the context
name|String
index|[]
name|names
init|=
name|applicationContext
operator|.
name|getBeanNamesForType
argument_list|(
name|SpringCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
operator|(
name|SpringCamelContext
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
name|names
index|[
literal|0
index|]
argument_list|,
name|SpringCamelContext
operator|.
name|class
argument_list|)
return|;
block|}
name|SpringCamelContext
name|answer
init|=
operator|new
name|SpringCamelContext
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|springCamelContext (String configLocations)
specifier|public
specifier|static
name|SpringCamelContext
name|springCamelContext
parameter_list|(
name|String
name|configLocations
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|springCamelContext
argument_list|(
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
name|configLocations
argument_list|)
argument_list|)
return|;
block|}
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets force lazy initialisation
name|getInjector
argument_list|()
expr_stmt|;
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|getApplicationContext ()
specifier|public
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createInjector ()
specifier|protected
name|Injector
name|createInjector
parameter_list|()
block|{
return|return
operator|new
name|SpringInjector
argument_list|(
operator|(
name|AbstractRefreshableApplicationContext
operator|)
name|getApplicationContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createComponentResolver ()
specifier|protected
name|ComponentResolver
name|createComponentResolver
parameter_list|()
block|{
name|ComponentResolver
name|defaultResolver
init|=
name|super
operator|.
name|createComponentResolver
argument_list|()
decl_stmt|;
return|return
operator|new
name|SpringComponentResolver
argument_list|(
name|getApplicationContext
argument_list|()
argument_list|,
name|defaultResolver
argument_list|)
return|;
block|}
block|}
end_class

end_unit

