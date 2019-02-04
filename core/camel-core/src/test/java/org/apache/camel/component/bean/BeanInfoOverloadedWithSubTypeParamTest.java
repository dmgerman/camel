begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|ContextTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BeanInfoOverloadedWithSubTypeParamTest
specifier|public
class|class
name|BeanInfoOverloadedWithSubTypeParamTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanInfoOverloadedWithSubTypedParam ()
specifier|public
name|void
name|testBeanInfoOverloadedWithSubTypedParam
parameter_list|()
block|{
name|BeanInfo
name|beanInfo
init|=
operator|new
name|BeanInfo
argument_list|(
name|context
argument_list|,
name|Bean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|beanInfo
operator|.
name|getMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|Bean
class|class
name|Bean
block|{
DECL|method|doSomething (RequestB request)
specifier|public
name|void
name|doSomething
parameter_list|(
name|RequestB
name|request
parameter_list|)
block|{         }
DECL|method|doSomething (RequestC request)
specifier|public
name|void
name|doSomething
parameter_list|(
name|RequestC
name|request
parameter_list|)
block|{         }
block|}
DECL|class|RequestB
class|class
name|RequestB
block|{     }
DECL|class|RequestC
class|class
name|RequestC
extends|extends
name|RequestB
block|{     }
block|}
end_class

end_unit

