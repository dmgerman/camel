begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Test
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
name|AbstractXmlApplicationContext
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

begin_class
DECL|class|ApplicationContextClassLoaderTest
specifier|public
class|class
name|ApplicationContextClassLoaderTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/disableJmxConfig.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testClassLoader ()
specifier|public
name|void
name|testClassLoader
parameter_list|()
block|{
name|ClassLoader
name|cl
init|=
name|context
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The application context class loader should not be null"
argument_list|,
name|cl
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong class loader"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|cl
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

