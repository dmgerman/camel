begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|jdbc
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|DataIntegrityViolationException
import|;
end_import

begin_comment
comment|/**  * A default {@link JdbcOptimisticLockingExceptionMapper} which checks the caused exception (and its nested)  * whether any of them is a constraint violation exception.  *<p/>  * The following check is done:  *<ul>  *<li>If the caused exception is an {@link SQLException}</li> then the SQLState is checked if starts with<tt>23</tt>.  *<li>If the caused exception is a {@link DataIntegrityViolationException}</li>  *<li>If the caused exception class name has<tt>ConstraintViolation</tt></li> in its name.  *<li>optional checking for FQN class name matches if any class names has been configured</li>  *</ul>  * In addition you can add FQN classnames using the {@link #addClassName(String)} or {@link #setClassNames(java.util.Set)}  * methods. These class names is also matched. This allows to add vendor specific exception classes.  */
end_comment

begin_class
DECL|class|DefaultJdbcOptimisticLockingExceptionMapper
specifier|public
class|class
name|DefaultJdbcOptimisticLockingExceptionMapper
implements|implements
name|JdbcOptimisticLockingExceptionMapper
block|{
DECL|field|classNames
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|classNames
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|isOptimisticLocking (Exception cause)
specifier|public
name|boolean
name|isOptimisticLocking
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Throwable
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createExceptionIterator
argument_list|(
name|cause
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Throwable
name|throwable
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// if its a SQL exception
if|if
condition|(
name|throwable
operator|instanceof
name|SQLException
condition|)
block|{
name|SQLException
name|se
init|=
operator|(
name|SQLException
operator|)
name|throwable
decl_stmt|;
if|if
condition|(
name|isConstraintViolation
argument_list|(
name|se
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|throwable
operator|instanceof
name|DataIntegrityViolationException
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// fallback to names
name|String
name|name
init|=
name|throwable
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"ConstraintViolation"
argument_list|)
operator|||
name|hasClassName
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|isConstraintViolation (SQLException e)
specifier|public
specifier|static
name|boolean
name|isConstraintViolation
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
return|return
name|e
operator|.
name|getSQLState
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"23"
argument_list|)
return|;
block|}
DECL|method|hasClassName (String name)
specifier|private
name|boolean
name|hasClassName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|String
name|className
range|:
name|classNames
control|)
block|{
if|if
condition|(
name|className
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|addClassName (String name)
specifier|public
name|void
name|addClassName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|classNames
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|setClassNames (Set<String> names)
specifier|public
name|void
name|setClassNames
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
name|classNames
operator|.
name|clear
argument_list|()
expr_stmt|;
name|classNames
operator|.
name|addAll
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

