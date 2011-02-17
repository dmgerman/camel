begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SuspendableServiceTest
specifier|public
class|class
name|SuspendableServiceTest
extends|extends
name|TestCase
block|{
DECL|class|MyService
specifier|private
class|class
name|MyService
implements|implements
name|SuspendableService
block|{
DECL|field|suspended
specifier|private
name|boolean
name|suspended
decl_stmt|;
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{         }
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
block|{
name|suspended
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|resume ()
specifier|public
name|void
name|resume
parameter_list|()
block|{
name|suspended
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|isSuspended ()
specifier|public
name|boolean
name|isSuspended
parameter_list|()
block|{
return|return
name|suspended
return|;
block|}
block|}
DECL|method|testSuspendable ()
specifier|public
name|void
name|testSuspendable
parameter_list|()
block|{
name|MyService
name|my
init|=
operator|new
name|MyService
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|my
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|my
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|my
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
name|my
operator|.
name|resume
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|my
operator|.
name|isSuspended
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

