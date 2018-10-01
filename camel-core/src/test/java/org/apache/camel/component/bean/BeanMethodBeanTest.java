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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|TestSupport
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
DECL|class|BeanMethodBeanTest
specifier|public
class|class
name|BeanMethodBeanTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testBeanMethod ()
specifier|public
name|void
name|testBeanMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|Method
name|method
init|=
name|MyFooBean
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"hello"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|MethodBean
name|mb
init|=
operator|new
name|MethodBean
argument_list|()
decl_stmt|;
name|mb
operator|.
name|setName
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|mb
operator|.
name|setType
argument_list|(
name|MyFooBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|mb
operator|.
name|setParameterTypes
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|mb
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|method
argument_list|,
name|mb
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|mb
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MyFooBean
operator|.
name|class
argument_list|,
name|mb
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

