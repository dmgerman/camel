begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|AsyncCallback
import|;
end_import

begin_comment
comment|/**  * An empty {@link AsyncCallback} which allows to share the same instance instead of creating a new instance for each message.  */
end_comment

begin_class
DECL|class|EmptyAsyncCallback
specifier|public
specifier|final
class|class
name|EmptyAsyncCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|EmptyAsyncCallback
name|INSTANCE
init|=
operator|new
name|EmptyAsyncCallback
argument_list|()
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|static
name|AsyncCallback
name|get
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
annotation|@
name|Override
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

