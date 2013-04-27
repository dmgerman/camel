begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|JdbcTemplate
import|;
end_import

begin_comment
comment|/**  * Used for unit testing  */
end_comment

begin_comment
comment|// START SNIPPET: e1
end_comment

begin_class
DECL|class|BookService
specifier|public
class|class
name|BookService
block|{
DECL|field|jdbc
specifier|private
name|JdbcTemplate
name|jdbc
decl_stmt|;
DECL|method|BookService ()
specifier|public
name|BookService
parameter_list|()
block|{     }
DECL|method|setDataSource (DataSource ds)
specifier|public
name|void
name|setDataSource
parameter_list|(
name|DataSource
name|ds
parameter_list|)
block|{
name|jdbc
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
expr_stmt|;
block|}
DECL|method|orderBook (String title)
specifier|public
name|void
name|orderBook
parameter_list|(
name|String
name|title
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|title
operator|.
name|startsWith
argument_list|(
literal|"Donkey"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"We don't have Donkeys, only Camels"
argument_list|)
throw|;
block|}
comment|// create new local datasource to store in DB
name|jdbc
operator|.
name|update
argument_list|(
literal|"insert into books (title) values (?)"
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: e1
end_comment

end_unit

