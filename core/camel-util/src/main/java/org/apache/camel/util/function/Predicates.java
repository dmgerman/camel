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
name|function
operator|.
name|Predicate
import|;
end_import

begin_comment
comment|/**  * Predicate helpers, inspired by http://minborgsjavapot.blogspot.it/2016/03/put-your-java-8-method-references-to.html  *  */
end_comment

begin_class
DECL|class|Predicates
specifier|public
specifier|final
class|class
name|Predicates
block|{
DECL|method|Predicates ()
specifier|private
name|Predicates
parameter_list|()
block|{     }
comment|/**      * Wrap a predicate, useful for method references.      */
DECL|method|of (Predicate<T> predicate)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|of
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|predicate
argument_list|,
literal|"Predicate must be specified"
argument_list|)
expr_stmt|;
return|return
name|predicate
return|;
block|}
comment|/**      * Negates a predicate, useful for method references.      *       *<pre>      *     Stream.of("A", "", "B")      *         .filter(Predicates.negate(String::isEmpty))      *         .count();      *</pre>      */
DECL|method|negate (Predicate<T> predicate)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Predicate
argument_list|<
name|T
argument_list|>
name|negate
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|)
block|{
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|predicate
argument_list|,
literal|"Predicate must be specified"
argument_list|)
expr_stmt|;
return|return
name|predicate
operator|.
name|negate
argument_list|()
return|;
block|}
block|}
end_class

end_unit

