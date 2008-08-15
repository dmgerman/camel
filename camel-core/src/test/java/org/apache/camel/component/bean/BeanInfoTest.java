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
name|ExchangePattern
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
name|InOnly
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
name|InOut
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
name|Pattern
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
comment|/**  * @version $Revision: 1.1 $  */
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
specifier|transient
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
specifier|protected
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|method|testMethodPatternUsingMethodAnnotations ()
specifier|public
name|void
name|testMethodPatternUsingMethodAnnotations
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOutMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"robustInOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
expr_stmt|;
block|}
DECL|method|testMethodPatternUsingClassAnnotationsOnInterface ()
specifier|public
name|void
name|testMethodPatternUsingClassAnnotationsOnInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|MyOneWayInterface
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
block|}
DECL|method|testMethodPatternUsingMethodAnnotationsOnInterface ()
specifier|public
name|void
name|testMethodPatternUsingMethodAnnotationsOnInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|MyOneWayInterfaceWithOverloadedMethod
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"robustInOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOutMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
DECL|method|testMethodPatternUsingClassAnnotationsButOverloadingOnMethod ()
specifier|public
name|void
name|testMethodPatternUsingClassAnnotationsButOverloadingOnMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|OverloadOnMethod
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"robustInOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
expr_stmt|;
block|}
DECL|method|testMethodPatternUsingClassAnnotationsButOverloadingOnBaseClassMethod ()
specifier|public
name|void
name|testMethodPatternUsingClassAnnotationsButOverloadingOnBaseClassMethod
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|OverloadOnBaseClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"robustInOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
expr_stmt|;
block|}
DECL|method|testMethodPatternUsingClassAnnotationsOnClassWithAnnotationsOnInterface ()
specifier|public
name|void
name|testMethodPatternUsingClassAnnotationsOnClassWithAnnotationsOnInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|OverloadOnMethod
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"robustInOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
expr_stmt|;
block|}
DECL|method|testMethodPatternUsingClassAnnotationsOnBaseInterfaceAndOverloadingMethodOnDerivedInterface ()
specifier|public
name|void
name|testMethodPatternUsingClassAnnotationsOnBaseInterfaceAndOverloadingMethodOnDerivedInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanInfo
name|info
init|=
name|createBeanInfo
argument_list|(
name|OverloadOnInterface
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"robustInOnlyMethod"
argument_list|,
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
expr_stmt|;
name|assertMethodPattern
argument_list|(
name|info
argument_list|,
literal|"inOutMethod"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
DECL|method|createBeanInfo (Class type)
specifier|protected
name|BeanInfo
name|createBeanInfo
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
name|BeanInfo
name|info
init|=
operator|new
name|BeanInfo
argument_list|(
name|camelContext
argument_list|,
name|type
argument_list|)
decl_stmt|;
return|return
name|info
return|;
block|}
DECL|method|assertMethodPattern (BeanInfo info, String methodName, ExchangePattern expectedPattern)
specifier|protected
name|void
name|assertMethodPattern
parameter_list|(
name|BeanInfo
name|info
parameter_list|,
name|String
name|methodName
parameter_list|,
name|ExchangePattern
name|expectedPattern
parameter_list|)
throws|throws
name|NoSuchMethodException
block|{
name|Class
name|type
init|=
name|info
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Method
name|method
init|=
name|type
operator|.
name|getMethod
argument_list|(
name|methodName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find method: "
operator|+
name|methodName
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|MethodInfo
name|methodInfo
init|=
name|info
operator|.
name|getMethodInfo
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find methodInfo for: "
operator|+
name|method
argument_list|,
name|methodInfo
argument_list|)
expr_stmt|;
name|ExchangePattern
name|actualPattern
init|=
name|methodInfo
operator|.
name|getPattern
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Pattern for: "
operator|+
name|method
argument_list|,
name|expectedPattern
argument_list|,
name|actualPattern
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Method: "
operator|+
name|method
operator|+
literal|" has pattern: "
operator|+
name|actualPattern
argument_list|)
expr_stmt|;
block|}
DECL|interface|Foo
specifier|public
interface|interface
name|Foo
block|{
DECL|method|inOutMethod ()
name|void
name|inOutMethod
parameter_list|()
function_decl|;
annotation|@
name|Pattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
DECL|method|inOnlyMethod ()
name|void
name|inOnlyMethod
parameter_list|()
function_decl|;
annotation|@
name|Pattern
argument_list|(
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
DECL|method|robustInOnlyMethod ()
name|void
name|robustInOnlyMethod
parameter_list|()
function_decl|;
block|}
annotation|@
name|InOnly
DECL|interface|MyOneWayInterface
specifier|public
interface|interface
name|MyOneWayInterface
block|{
DECL|method|inOnlyMethod ()
name|void
name|inOnlyMethod
parameter_list|()
function_decl|;
block|}
annotation|@
name|InOnly
DECL|interface|MyOneWayInterfaceWithOverloadedMethod
specifier|public
interface|interface
name|MyOneWayInterfaceWithOverloadedMethod
block|{
DECL|method|inOnlyMethod ()
name|void
name|inOnlyMethod
parameter_list|()
function_decl|;
annotation|@
name|Pattern
argument_list|(
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
DECL|method|robustInOnlyMethod ()
name|void
name|robustInOnlyMethod
parameter_list|()
function_decl|;
annotation|@
name|InOut
DECL|method|inOutMethod ()
name|Object
name|inOutMethod
parameter_list|()
function_decl|;
block|}
DECL|class|OverloadOnMethod
specifier|public
specifier|static
class|class
name|OverloadOnMethod
implements|implements
name|MyOneWayInterface
block|{
DECL|method|inOnlyMethod ()
specifier|public
name|void
name|inOnlyMethod
parameter_list|()
block|{         }
annotation|@
name|Pattern
argument_list|(
name|ExchangePattern
operator|.
name|RobustInOnly
argument_list|)
DECL|method|robustInOnlyMethod ()
specifier|public
name|void
name|robustInOnlyMethod
parameter_list|()
block|{         }
block|}
DECL|class|OverloadOnBaseClass
specifier|public
specifier|static
class|class
name|OverloadOnBaseClass
extends|extends
name|OverloadOnMethod
block|{
DECL|method|robustInOnlyMethod ()
specifier|public
name|void
name|robustInOnlyMethod
parameter_list|()
block|{         }
block|}
DECL|class|OverloadOnInterface
specifier|public
specifier|static
class|class
name|OverloadOnInterface
implements|implements
name|MyOneWayInterfaceWithOverloadedMethod
block|{
DECL|method|inOnlyMethod ()
specifier|public
name|void
name|inOnlyMethod
parameter_list|()
block|{         }
DECL|method|robustInOnlyMethod ()
specifier|public
name|void
name|robustInOnlyMethod
parameter_list|()
block|{         }
DECL|method|inOutMethod ()
specifier|public
name|Object
name|inOutMethod
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

