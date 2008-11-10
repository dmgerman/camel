begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|event
package|;
end_package

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
name|impl
operator|.
name|DefaultComponent
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
name|ConfigurableApplicationContext
import|;
end_import

begin_comment
comment|/**  * An<a href="http://activemq.apache.org/camel/event.html">Event Component</a>  * for working with Spring ApplicationEvents  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|EventComponent
specifier|public
class|class
name|EventComponent
extends|extends
name|DefaultComponent
implements|implements
name|ApplicationContextAware
block|{
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|method|EventComponent ()
specifier|public
name|EventComponent
parameter_list|()
block|{     }
DECL|method|EventComponent (ApplicationContext applicationContext)
specifier|public
name|EventComponent
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|setApplicationContext
argument_list|(
name|applicationContext
argument_list|)
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
DECL|method|getConfigurableApplicationContext ()
specifier|public
name|ConfigurableApplicationContext
name|getConfigurableApplicationContext
parameter_list|()
block|{
name|ApplicationContext
name|applicationContext
init|=
name|getApplicationContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|applicationContext
operator|instanceof
name|ConfigurableApplicationContext
condition|)
block|{
return|return
operator|(
name|ConfigurableApplicationContext
operator|)
name|applicationContext
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Not created with a ConfigurableApplicationContext! Was: "
operator|+
name|applicationContext
argument_list|)
throw|;
block|}
block|}
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|EventEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|EventEndpoint
name|answer
init|=
operator|new
name|EventEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
comment|// getConfigurableApplicationContext().addApplicationListener(answer);
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

