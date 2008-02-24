begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.bind
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|bind
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|component
operator|.
name|bean
operator|.
name|BeanInfo
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
name|BeanProcessor
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
name|DefaultParameterMappingStrategy
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
name|MethodInvocation
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BeanInfoTest
specifier|public
class|class
name|BeanInfoTest
extends|extends
name|TestCase
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|BeanInfoTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|DefaultCamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|exchange
specifier|protected
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
DECL|field|strategy
specifier|protected
name|DefaultParameterMappingStrategy
name|strategy
init|=
operator|new
name|DefaultParameterMappingStrategy
argument_list|()
decl_stmt|;
DECL|field|bean
specifier|protected
name|ExampleBean
name|bean
init|=
operator|new
name|ExampleBean
argument_list|()
decl_stmt|;
DECL|field|info
specifier|protected
name|BeanInfo
name|info
decl_stmt|;
DECL|method|testFindsSingleMethodMatchingBody ()
specifier|public
name|void
name|testFindsSingleMethodMatchingBody
parameter_list|()
throws|throws
name|Throwable
block|{
name|MethodInvocation
name|invocation
init|=
name|info
operator|.
name|createInvocation
argument_list|(
name|bean
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found a method invocation!"
argument_list|,
name|invocation
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|invocation
operator|.
name|proceed
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Value: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testBeanProcessor ()
specifier|public
name|void
name|testBeanProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanProcessor
name|processor
init|=
operator|new
name|BeanProcessor
argument_list|(
name|bean
argument_list|,
name|info
argument_list|)
decl_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Exchange is: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|info
operator|=
operator|new
name|BeanInfo
argument_list|(
name|camelContext
argument_list|,
name|bean
operator|.
name|getClass
argument_list|()
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

