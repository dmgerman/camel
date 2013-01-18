begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|sql
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  * Bean that creates the database table  */
end_comment

begin_class
DECL|class|DatabaseBean
specifier|public
class|class
name|DatabaseBean
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DatabaseBean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
DECL|method|getDataSource ()
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
DECL|method|setDataSource (DataSource dataSource)
specifier|public
name|void
name|setDataSource
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
DECL|method|create ()
specifier|public
name|void
name|create
parameter_list|()
throws|throws
name|Exception
block|{
name|JdbcTemplate
name|jdbc
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|dataSource
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
literal|"create table orders (\n"
operator|+
literal|"  id integer primary key,\n"
operator|+
literal|"  item varchar(10),\n"
operator|+
literal|"  amount varchar(5),\n"
operator|+
literal|"  description varchar(30),\n"
operator|+
literal|"  processed boolean\n"
operator|+
literal|")"
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating table orders ..."
argument_list|)
expr_stmt|;
name|jdbc
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"... created table orders"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

