begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|Exchange
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
name|Predicate
import|;
end_import

begin_comment
comment|/**  * A helper for doing {@link Predicate} assertions.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|PredicateAssertHelper
specifier|public
specifier|final
class|class
name|PredicateAssertHelper
block|{
DECL|method|PredicateAssertHelper ()
specifier|private
name|PredicateAssertHelper
parameter_list|()
block|{
comment|// Utility class
block|}
DECL|method|assertMatches (Predicate predicate, String text, Exchange exchange)
specifier|public
specifier|static
name|void
name|assertMatches
parameter_list|(
name|Predicate
name|predicate
parameter_list|,
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|predicate
operator|+
literal|" on "
operator|+
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|text
operator|+
name|predicate
operator|+
literal|" on "
operator|+
name|exchange
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

