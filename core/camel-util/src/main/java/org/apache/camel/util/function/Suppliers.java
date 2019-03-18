begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.function
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|function
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
import|;
end_import

begin_class
DECL|class|Suppliers
specifier|public
specifier|final
class|class
name|Suppliers
block|{
DECL|method|Suppliers ()
specifier|private
name|Suppliers
parameter_list|()
block|{     }
DECL|method|memorize (Supplier<T> supplier)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Supplier
argument_list|<
name|T
argument_list|>
name|memorize
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|supplier
parameter_list|)
block|{
specifier|final
name|AtomicReference
argument_list|<
name|T
argument_list|>
name|valueHolder
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
return|return
parameter_list|()
lambda|->
block|{
name|T
name|supplied
init|=
name|valueHolder
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|supplied
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|valueHolder
init|)
block|{
name|supplied
operator|=
name|valueHolder
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|supplied
operator|==
literal|null
condition|)
block|{
name|supplied
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|supplier
operator|.
name|get
argument_list|()
argument_list|,
literal|"Supplier should not return null"
argument_list|)
expr_stmt|;
name|valueHolder
operator|.
name|lazySet
argument_list|(
name|supplied
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|supplied
return|;
block|}
return|;
block|}
DECL|method|memorize (ThrowingSupplier<T, ? extends Exception> supplier, Consumer<Exception> consumer)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Supplier
argument_list|<
name|T
argument_list|>
name|memorize
parameter_list|(
name|ThrowingSupplier
argument_list|<
name|T
argument_list|,
name|?
extends|extends
name|Exception
argument_list|>
name|supplier
parameter_list|,
name|Consumer
argument_list|<
name|Exception
argument_list|>
name|consumer
parameter_list|)
block|{
specifier|final
name|AtomicReference
argument_list|<
name|T
argument_list|>
name|valueHolder
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
return|return
parameter_list|()
lambda|->
block|{
name|T
name|supplied
init|=
name|valueHolder
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|supplied
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|valueHolder
init|)
block|{
name|supplied
operator|=
name|valueHolder
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|supplied
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|supplied
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|supplier
operator|.
name|get
argument_list|()
argument_list|,
literal|"Supplier should not return null"
argument_list|)
expr_stmt|;
name|valueHolder
operator|.
name|lazySet
argument_list|(
name|supplied
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|supplied
return|;
block|}
return|;
block|}
DECL|method|firstNotNull (ThrowingSupplier<T, Exception>.... suppliers)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|firstNotNull
parameter_list|(
name|ThrowingSupplier
argument_list|<
name|T
argument_list|,
name|Exception
argument_list|>
modifier|...
name|suppliers
parameter_list|)
throws|throws
name|Exception
block|{
name|T
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ThrowingSupplier
argument_list|<
name|T
argument_list|,
name|Exception
argument_list|>
name|supplier
range|:
name|suppliers
control|)
block|{
name|answer
operator|=
name|supplier
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|answer
argument_list|)
return|;
block|}
DECL|method|firstMatching (Predicate<T> predicate, ThrowingSupplier<T, Exception>... suppliers)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|firstMatching
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|,
name|ThrowingSupplier
argument_list|<
name|T
argument_list|,
name|Exception
argument_list|>
modifier|...
name|suppliers
parameter_list|)
throws|throws
name|Exception
block|{
name|T
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ThrowingSupplier
argument_list|<
name|T
argument_list|,
name|Exception
argument_list|>
name|supplier
range|:
name|suppliers
control|)
block|{
name|answer
operator|=
name|supplier
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|predicate
operator|.
name|test
argument_list|(
name|answer
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|answer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

