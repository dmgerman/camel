begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
package|;
end_package

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdErrorCode
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdException
import|;
end_import

begin_class
DECL|class|EtcdHelper
specifier|public
specifier|final
class|class
name|EtcdHelper
block|{
DECL|field|OUTDATED_EVENT_MSG
specifier|private
specifier|static
specifier|final
name|String
name|OUTDATED_EVENT_MSG
init|=
literal|"requested index is outdated and cleared"
decl_stmt|;
DECL|method|EtcdHelper ()
specifier|private
name|EtcdHelper
parameter_list|()
block|{     }
DECL|method|isOutdatedIndexException (EtcdException exception)
specifier|public
specifier|static
name|boolean
name|isOutdatedIndexException
parameter_list|(
name|EtcdException
name|exception
parameter_list|)
block|{
if|if
condition|(
name|exception
operator|.
name|isErrorCode
argument_list|(
name|EtcdErrorCode
operator|.
name|EventIndexCleared
argument_list|)
operator|&&
name|exception
operator|.
name|etcdMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|exception
operator|.
name|etcdMessage
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
name|OUTDATED_EVENT_MSG
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

