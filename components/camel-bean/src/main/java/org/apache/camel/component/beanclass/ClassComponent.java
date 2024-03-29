begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.beanclass
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|beanclass
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
name|Endpoint
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
name|component
operator|.
name|bean
operator|.
name|BeanComponent
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
name|component
operator|.
name|bean
operator|.
name|BeanHolder
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
name|component
operator|.
name|bean
operator|.
name|ConstantBeanHolder
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
name|component
operator|.
name|bean
operator|.
name|ConstantTypeBeanHolder
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
name|PropertiesHelper
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/class.html">Class Component</a> is for binding JavaBeans to Camel message exchanges based on class name.  *<p/>  * This component is an extension to the {@link org.apache.camel.component.bean.BeanComponent}.  */
end_comment

begin_class
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|annotations
operator|.
name|Component
argument_list|(
literal|"class"
argument_list|)
DECL|class|ClassComponent
specifier|public
class|class
name|ClassComponent
extends|extends
name|BeanComponent
block|{
DECL|method|ClassComponent ()
specifier|public
name|ClassComponent
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|ClassEndpoint
name|endpoint
init|=
operator|new
name|ClassEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setBeanName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
comment|// bean name is the FQN
name|String
name|name
init|=
name|endpoint
operator|.
name|getBeanName
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// the bean.xxx options is for the bean
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
name|PropertiesHelper
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"bean."
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setParameters
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|BeanHolder
name|holder
decl_stmt|;
comment|// if there is options then we need to create a bean instance
if|if
condition|(
operator|!
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// create bean
name|Object
name|bean
init|=
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
comment|// now set additional properties on it
name|setProperties
argument_list|(
name|bean
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|holder
operator|=
operator|new
name|ConstantBeanHolder
argument_list|(
name|bean
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// otherwise refer to the type
name|holder
operator|=
operator|new
name|ConstantTypeBeanHolder
argument_list|(
name|clazz
argument_list|,
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|options
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// and register the bean as a holder on the endpoint
name|endpoint
operator|.
name|setBeanHolder
argument_list|(
name|holder
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

