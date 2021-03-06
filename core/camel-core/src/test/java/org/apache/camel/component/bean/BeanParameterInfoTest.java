begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Body
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
name|ContextTestSupport
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
name|DefaultCamelContext
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
DECL|class|BeanParameterInfoTest
specifier|public
class|class
name|BeanParameterInfoTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testMethodPatternUsingMethodAnnotations ()
specifier|public
name|void
name|testMethodPatternUsingMethodAnnotations
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|foo
init|=
name|Foo
operator|.
name|class
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|ParameterInfo
name|info
init|=
operator|new
name|ParameterInfo
argument_list|(
literal|1
argument_list|,
name|foo
operator|.
name|getClass
argument_list|()
argument_list|,
name|foo
operator|.
name|getAnnotations
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|info
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|info
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|foo
argument_list|,
name|info
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|info
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|info
operator|.
name|getAnnotations
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|Foo
specifier|private
specifier|static
class|class
name|Foo
block|{
DECL|method|hello (@ody String body)
specifier|public
name|String
name|hello
parameter_list|(
annotation|@
name|Body
name|String
name|body
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|body
return|;
block|}
block|}
block|}
end_class

end_unit

