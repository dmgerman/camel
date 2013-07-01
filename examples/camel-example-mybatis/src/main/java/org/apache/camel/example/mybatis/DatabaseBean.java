begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|mybatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|CamelContextAware
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
name|component
operator|.
name|mybatis
operator|.
name|MyBatisComponent
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

begin_comment
comment|/**  * Bean that creates the database table  */
end_comment

begin_class
DECL|class|DatabaseBean
specifier|public
class|class
name|DatabaseBean
implements|implements
name|CamelContextAware
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
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
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
name|String
name|sql
init|=
literal|"create table ORDERS (\n"
operator|+
literal|"  ORD_ID integer primary key,\n"
operator|+
literal|"  ITEM varchar(10),\n"
operator|+
literal|"  ITEM_COUNT varchar(5),\n"
operator|+
literal|"  ITEM_DESC varchar(30),\n"
operator|+
literal|"  ORD_DELETED boolean\n"
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
try|try
block|{
name|execute
argument_list|(
literal|"drop table orders"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
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
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|execute
argument_list|(
literal|"drop table orders"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
DECL|method|execute (String sql)
specifier|private
name|void
name|execute
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
name|MyBatisComponent
name|component
init|=
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"mybatis"
argument_list|,
name|MyBatisComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Connection
name|con
init|=
name|component
operator|.
name|getSqlSessionFactory
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getEnvironment
argument_list|()
operator|.
name|getDataSource
argument_list|()
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|Statement
name|stm
init|=
name|con
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|stm
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
comment|// must commit connection
name|con
operator|.
name|commit
argument_list|()
expr_stmt|;
name|stm
operator|.
name|close
argument_list|()
expr_stmt|;
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

