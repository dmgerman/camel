begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|ObjectPool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|PoolableObjectFactory
import|;
end_import

begin_comment
comment|/**  * An {@link org.apache.commons.pool.ObjectPool} that uses a single shared instance.  *<p/>  * This implementation will always return<tt>1</tt> in {@link #getNumActive()} and  * return<tt>0</tt> in {@link #getNumIdle()}.  */
end_comment

begin_class
DECL|class|SharedSingletonObjectPool
specifier|public
class|class
name|SharedSingletonObjectPool
parameter_list|<
name|T
parameter_list|>
implements|implements
name|ObjectPool
argument_list|<
name|T
argument_list|>
block|{
DECL|field|factory
specifier|private
specifier|final
name|PoolableObjectFactory
argument_list|<
name|T
argument_list|>
name|factory
decl_stmt|;
DECL|field|t
specifier|private
specifier|volatile
name|T
name|t
decl_stmt|;
DECL|method|SharedSingletonObjectPool (PoolableObjectFactory<T> factory)
specifier|public
name|SharedSingletonObjectPool
parameter_list|(
name|PoolableObjectFactory
argument_list|<
name|T
argument_list|>
name|factory
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|borrowObject ()
specifier|public
specifier|synchronized
name|T
name|borrowObject
parameter_list|()
throws|throws
name|Exception
throws|,
name|NoSuchElementException
throws|,
name|IllegalStateException
block|{
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
name|factory
operator|.
name|makeObject
argument_list|()
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
annotation|@
name|Override
DECL|method|returnObject (T obj)
specifier|public
name|void
name|returnObject
parameter_list|(
name|T
name|obj
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|invalidateObject (T obj)
specifier|public
name|void
name|invalidateObject
parameter_list|(
name|T
name|obj
parameter_list|)
throws|throws
name|Exception
block|{
name|t
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addObject ()
specifier|public
name|void
name|addObject
parameter_list|()
throws|throws
name|Exception
throws|,
name|IllegalStateException
throws|,
name|UnsupportedOperationException
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|getNumIdle ()
specifier|public
name|int
name|getNumIdle
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|getNumActive ()
specifier|public
name|int
name|getNumActive
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
throws|throws
name|Exception
throws|,
name|UnsupportedOperationException
block|{
name|t
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
name|t
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setFactory (PoolableObjectFactory<T> factory)
specifier|public
name|void
name|setFactory
parameter_list|(
name|PoolableObjectFactory
argument_list|<
name|T
argument_list|>
name|factory
parameter_list|)
throws|throws
name|IllegalStateException
throws|,
name|UnsupportedOperationException
block|{
comment|// noop
block|}
block|}
end_class

end_unit

