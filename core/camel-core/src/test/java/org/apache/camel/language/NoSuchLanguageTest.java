begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
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
name|LanguageTestSupport
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
name|NoSuchLanguageException
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
DECL|class|NoSuchLanguageTest
specifier|public
class|class
name|NoSuchLanguageTest
extends|extends
name|LanguageTestSupport
block|{
annotation|@
name|Test
DECL|method|testNoSuchLanguage ()
specifier|public
name|void
name|testNoSuchLanguage
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|assertPredicate
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchLanguageException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"No language could be found for: unknown"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unknown"
argument_list|,
name|e
operator|.
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getLanguageName ()
specifier|protected
name|String
name|getLanguageName
parameter_list|()
block|{
return|return
literal|"unknown"
return|;
block|}
block|}
end_class

end_unit

