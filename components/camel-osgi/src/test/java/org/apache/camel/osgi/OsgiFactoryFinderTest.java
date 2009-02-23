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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|NoFactoryAvailableException
import|;
end_import

begin_class
DECL|class|OsgiFactoryFinderTest
specifier|public
class|class
name|OsgiFactoryFinderTest
extends|extends
name|CamelOsgiTestSupport
block|{
DECL|method|testFindClass ()
specifier|public
name|void
name|testFindClass
parameter_list|()
throws|throws
name|Exception
block|{
name|OsgiFactoryFinder
name|finder
init|=
operator|new
name|OsgiFactoryFinder
argument_list|(
literal|"META-INF/services/org/apache/camel/component/"
argument_list|)
decl_stmt|;
name|Class
name|clazz
init|=
name|finder
operator|.
name|findClass
argument_list|(
literal|"file_test"
argument_list|,
literal|"strategy.factory."
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the file strategy factory here"
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
try|try
block|{
name|clazz
operator|=
name|finder
operator|.
name|findClass
argument_list|(
literal|"nofile"
argument_list|,
literal|"strategy.factory."
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We should get exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should get NoFactoryAvailableException"
argument_list|,
name|ex
operator|instanceof
name|NoFactoryAvailableException
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|clazz
operator|=
name|finder
operator|.
name|findClass
argument_list|(
literal|"file_test"
argument_list|,
literal|"nostrategy.factory."
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We should get exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Should get IOException"
argument_list|,
name|ex
operator|instanceof
name|IOException
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

