begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|spi
package|;
end_package

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
name|wiring
operator|.
name|BeanConfigurerSupport
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

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|BeanInjector
specifier|public
class|class
name|BeanInjector
extends|extends
name|BeanConfigurerSupport
block|{
DECL|method|BeanInjector (ApplicationContext applicationContext)
specifier|public
name|BeanInjector
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|Exception
block|{
name|setBeanFactory
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|afterPropertiesSet
argument_list|()
expr_stmt|;
block|}
DECL|method|inject (Object bean)
specifier|public
name|void
name|inject
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
name|configureBean
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

