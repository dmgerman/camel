begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

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
name|DriverManager
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
end_import

begin_comment
comment|/**  * TODO Provide description for TestDataSource.  *   * @author<a href="mailto:nsandhu@raleys.com">nsandhu</a>  *  */
end_comment

begin_class
DECL|class|TestDataSource
specifier|public
class|class
name|TestDataSource
implements|implements
name|DataSource
block|{
DECL|field|url
DECL|field|username
DECL|field|password
specifier|private
name|String
name|url
decl_stmt|,
name|username
decl_stmt|,
name|password
decl_stmt|;
DECL|method|TestDataSource (String url, String user, String password)
specifier|public
name|TestDataSource
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|user
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
DECL|method|getConnection (String username, String password)
specifier|public
name|Connection
name|getConnection
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
DECL|method|getLoginTimeout ()
specifier|public
name|int
name|getLoginTimeout
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|DriverManager
operator|.
name|getLoginTimeout
argument_list|()
return|;
block|}
DECL|method|getLogWriter ()
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|DriverManager
operator|.
name|getLogWriter
argument_list|()
return|;
block|}
DECL|method|setLoginTimeout (int seconds)
specifier|public
name|void
name|setLoginTimeout
parameter_list|(
name|int
name|seconds
parameter_list|)
throws|throws
name|SQLException
block|{
name|DriverManager
operator|.
name|setLoginTimeout
argument_list|(
name|seconds
argument_list|)
expr_stmt|;
block|}
DECL|method|setLogWriter (PrintWriter out)
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|SQLException
block|{
name|DriverManager
operator|.
name|setLogWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|isWrapperFor (Class<?> iface)
specifier|public
name|boolean
name|isWrapperFor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// TODO Auto-generated method stub
return|return
literal|false
return|;
block|}
DECL|method|unwrap (Class<T> iface)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|iface
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

