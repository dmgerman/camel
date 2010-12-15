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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|IdempotentRepository
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JdbcIdempotentRepository
specifier|public
class|class
name|JdbcIdempotentRepository
implements|implements
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
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
DECL|method|add (String key)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// check we already have it because eager option can have been turned on
if|if
condition|(
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|jdbc
operator|.
name|update
argument_list|(
literal|"INSERT INTO ProcessedPayments (paymentIdentifier) VALUES (?)"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|int
name|numMatches
init|=
name|jdbc
operator|.
name|queryForInt
argument_list|(
literal|"SELECT count(0) FROM ProcessedPayments where paymentIdentifier = ?"
argument_list|,
name|key
argument_list|)
decl_stmt|;
return|return
name|numMatches
operator|>
literal|0
return|;
block|}
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|jdbc
operator|.
name|update
argument_list|(
literal|"DELETE FROM ProcessedPayments WHERE paymentIdentifier = ?"
argument_list|,
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|confirm (String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// noop
return|return
literal|true
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

