begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|sql
package|;
end_package

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

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_class
DECL|class|DatabaseManager
specifier|public
class|class
name|DatabaseManager
block|{
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
DECL|method|DatabaseManager (DataSource dataSource)
specifier|public
name|DatabaseManager
parameter_list|(
name|DataSource
name|dataSource
parameter_list|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
block|}
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|JdbcTemplate
name|jdbcTemplate
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|dataSource
argument_list|)
decl_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"create table projects (id integer primary key,"
operator|+
literal|"project varchar(10), license varchar(5))"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (1, 'Camel', 'ASF')"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (2, 'AMQ', 'ASF')"
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"insert into projects values (3, 'Linux', 'XXX')"
argument_list|)
expr_stmt|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|JdbcTemplate
name|jdbcTemplate
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|dataSource
argument_list|)
decl_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
literal|"drop table projects"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

