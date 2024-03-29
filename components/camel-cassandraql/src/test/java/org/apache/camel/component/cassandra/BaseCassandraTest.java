begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cassandra
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cassandra
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_class
DECL|class|BaseCassandraTest
specifier|public
specifier|abstract
class|class
name|BaseCassandraTest
extends|extends
name|CamelTestSupport
block|{
DECL|method|canTest ()
specifier|public
specifier|static
name|boolean
name|canTest
parameter_list|()
block|{
comment|// we cannot test on CI
return|return
name|System
operator|.
name|getenv
argument_list|(
literal|"BUILD_ID"
argument_list|)
operator|==
literal|null
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|setUpClass ()
specifier|public
specifier|static
name|void
name|setUpClass
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|canTest
argument_list|()
condition|)
block|{
name|CassandraUnitUtils
operator|.
name|startEmbeddedCassandra
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|AfterClass
DECL|method|tearDownClass ()
specifier|public
specifier|static
name|void
name|tearDownClass
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|canTest
argument_list|()
condition|)
block|{
try|try
block|{
name|CassandraUnitUtils
operator|.
name|cleanEmbeddedCassandra
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore shutdown errors
block|}
block|}
block|}
block|}
end_class

end_unit

