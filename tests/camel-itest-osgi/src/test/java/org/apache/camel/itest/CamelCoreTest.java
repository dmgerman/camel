begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|test
operator|.
name|karaf
operator|.
name|CamelKarafTestSupport
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
name|ObjectHelper
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
DECL|class|CamelCoreTest
specifier|public
class|class
name|CamelCoreTest
extends|extends
name|CamelKarafTestSupport
block|{
annotation|@
name|Test
DECL|method|testCamelCore ()
specifier|public
name|void
name|testCamelCore
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
literal|"org/apache/camel/itest/CamelCoreTest.xml"
argument_list|,
name|CamelCoreTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|">>>> "
operator|+
name|url
argument_list|)
expr_stmt|;
name|installBlueprintAsBundle
argument_list|(
literal|"CamelCoreTest"
argument_list|,
name|url
argument_list|)
expr_stmt|;
comment|// wait for Camel to be ready
comment|//        CamelContext camel = getOsgiService(CamelContext.class);
comment|//        System.out.println(">>> " + camel);
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
name|Option
index|[]
name|configure
parameter_list|()
block|{
return|return
name|CamelKarafTestSupport
operator|.
name|configure
argument_list|()
return|;
block|}
block|}
end_class

end_unit

