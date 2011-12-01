begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|idempotent
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
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|JdbcTableService
specifier|public
class|class
name|JdbcTableService
implements|implements
name|InitializingBean
implements|,
name|DisposableBean
block|{
DECL|field|jdbc
specifier|private
name|JdbcTemplate
name|jdbc
decl_stmt|;
DECL|method|setDataSource (DataSource ds)
specifier|public
name|void
name|setDataSource
parameter_list|(
name|DataSource
name|ds
parameter_list|)
block|{
name|this
operator|.
name|jdbc
operator|=
operator|new
name|JdbcTemplate
argument_list|(
name|ds
argument_list|)
expr_stmt|;
block|}
DECL|method|createTable ()
specifier|public
name|void
name|createTable
parameter_list|()
block|{
try|try
block|{
name|jdbc
operator|.
name|execute
argument_list|(
literal|"drop table ProcessedPayments"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|jdbc
operator|.
name|execute
argument_list|(
literal|"create table ProcessedPayments (paymentIdentifier varchar(24))"
argument_list|)
expr_stmt|;
block|}
DECL|method|dropTable ()
specifier|public
name|void
name|dropTable
parameter_list|()
block|{
name|jdbc
operator|.
name|execute
argument_list|(
literal|"drop table ProcessedPayments"
argument_list|)
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|dropTable
argument_list|()
expr_stmt|;
block|}
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|createTable
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

