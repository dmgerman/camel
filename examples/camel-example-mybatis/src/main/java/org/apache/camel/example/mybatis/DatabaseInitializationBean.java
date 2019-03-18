begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|derby
operator|.
name|jdbc
operator|.
name|EmbeddedDriver
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
DECL|class|DatabaseInitializationBean
specifier|public
class|class
name|DatabaseInitializationBean
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
name|DatabaseInitializationBean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|url
name|String
name|url
decl_stmt|;
DECL|field|connection
name|Connection
name|connection
decl_stmt|;
DECL|method|DatabaseInitializationBean ()
specifier|public
name|DatabaseInitializationBean
parameter_list|()
block|{     }
DECL|method|create ()
specifier|public
name|void
name|create
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating database tables ..."
argument_list|)
expr_stmt|;
if|if
condition|(
name|connection
operator|==
literal|null
condition|)
block|{
name|EmbeddedDriver
name|driver
init|=
operator|new
name|EmbeddedDriver
argument_list|()
decl_stmt|;
name|connection
operator|=
name|driver
operator|.
name|connect
argument_list|(
name|url
operator|+
literal|";create=true"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
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
literal|"Database tables created"
argument_list|)
expr_stmt|;
block|}
DECL|method|drop ()
specifier|public
name|void
name|drop
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Dropping database tables ..."
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
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Database tables dropped"
argument_list|)
expr_stmt|;
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
name|Statement
name|stm
init|=
name|connection
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
name|connection
operator|.
name|commit
argument_list|()
expr_stmt|;
name|stm
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
block|}
end_class

end_unit

