begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IMap
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
import|;
end_import

begin_class
DECL|class|HazelcastIdempotentRepository
specifier|public
class|class
name|HazelcastIdempotentRepository
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
block|{
DECL|field|repositoryName
specifier|private
name|String
name|repositoryName
decl_stmt|;
DECL|field|repo
specifier|private
name|IMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|repo
decl_stmt|;
DECL|field|hazelcastInstance
specifier|private
name|HazelcastInstance
name|hazelcastInstance
decl_stmt|;
DECL|method|HazelcastIdempotentRepository (HazelcastInstance hazelcastInstance)
specifier|public
name|HazelcastIdempotentRepository
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|this
argument_list|(
name|hazelcastInstance
argument_list|,
name|HazelcastIdempotentRepository
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|HazelcastIdempotentRepository (HazelcastInstance hazelcastInstance, String repositoryName)
specifier|public
name|HazelcastIdempotentRepository
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|String
name|repositoryName
parameter_list|)
block|{
name|this
operator|.
name|repositoryName
operator|=
name|repositoryName
expr_stmt|;
name|this
operator|.
name|hazelcastInstance
operator|=
name|hazelcastInstance
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|repo
operator|=
name|hazelcastInstance
operator|.
name|getMap
argument_list|(
name|repositoryName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|add (String key)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
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
else|else
block|{
name|this
operator|.
name|repo
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|confirm (String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|this
operator|.
name|repo
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|this
operator|.
name|repo
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|this
operator|.
name|repo
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|getRepositoryName ()
specifier|public
name|String
name|getRepositoryName
parameter_list|()
block|{
return|return
name|repositoryName
return|;
block|}
block|}
end_class

end_unit

