begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
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
name|springframework
operator|.
name|osgi
operator|.
name|mock
operator|.
name|MockBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|osgi
operator|.
name|mock
operator|.
name|MockBundleContext
import|;
end_import

begin_class
DECL|class|ActivatorTest
specifier|public
class|class
name|ActivatorTest
extends|extends
name|CamelOsgiTestSupport
block|{
DECL|method|testGetComponent ()
specifier|public
name|void
name|testGetComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|clazz
init|=
name|Activator
operator|.
name|getComponent
argument_list|(
literal|"timer"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"Here should not find the timer component"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|clazz
operator|=
literal|null
expr_stmt|;
name|clazz
operator|=
name|Activator
operator|.
name|getComponent
argument_list|(
literal|"timer_test"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The timer_test component should not be null"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetLanaguge ()
specifier|public
name|void
name|testGetLanaguge
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|clazz
init|=
name|Activator
operator|.
name|getLanguage
argument_list|(
literal|"bean_test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The bean_test component should not be null"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
DECL|method|containsPackageName (String packageName, String[] packages)
specifier|private
name|boolean
name|containsPackageName
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
index|[]
name|packages
parameter_list|)
block|{
for|for
control|(
name|String
name|name
range|:
name|packages
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|packageName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|testFindTypeConverterPackageNames ()
specifier|public
name|void
name|testFindTypeConverterPackageNames
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|packages
init|=
name|Activator
operator|.
name|findTypeConverterPackageNames
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should find three converter package here"
argument_list|,
literal|3
argument_list|,
name|packages
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Here should contains org.apache.camel.osgi.test"
argument_list|,
name|containsPackageName
argument_list|(
literal|"org.apache.camel.osgi.test"
argument_list|,
name|packages
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

