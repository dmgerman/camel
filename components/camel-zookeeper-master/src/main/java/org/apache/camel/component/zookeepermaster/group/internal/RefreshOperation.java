begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster.group.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
operator|.
name|group
operator|.
name|internal
package|;
end_package

begin_class
DECL|class|RefreshOperation
class|class
name|RefreshOperation
implements|implements
name|Operation
block|{
DECL|field|cache
specifier|private
specifier|final
name|ZooKeeperGroup
name|cache
decl_stmt|;
DECL|field|mode
specifier|private
specifier|final
name|ZooKeeperGroup
operator|.
name|RefreshMode
name|mode
decl_stmt|;
DECL|method|RefreshOperation (ZooKeeperGroup cache, ZooKeeperGroup.RefreshMode mode)
name|RefreshOperation
parameter_list|(
name|ZooKeeperGroup
name|cache
parameter_list|,
name|ZooKeeperGroup
operator|.
name|RefreshMode
name|mode
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invoke ()
specifier|public
name|void
name|invoke
parameter_list|()
throws|throws
name|Exception
block|{
name|cache
operator|.
name|refresh
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RefreshOperation
name|that
init|=
operator|(
name|RefreshOperation
operator|)
name|o
decl_stmt|;
comment|//noinspection RedundantIfStatement
if|if
condition|(
name|mode
operator|!=
name|that
operator|.
name|mode
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|mode
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RefreshOperation("
operator|+
name|mode
operator|+
literal|"){}"
return|;
block|}
block|}
end_class

end_unit

