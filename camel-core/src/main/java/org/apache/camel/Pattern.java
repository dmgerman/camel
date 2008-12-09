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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_comment
comment|/**  * Marks a method as having a specific kind of {@link ExchangePattern} for use with  *<a href="http://activemq.apache.org/camel/bean-integration.html">Bean Integration</a> or  *<a href="http://activemq.apache.org/camel/spring-remoting.html">Spring Remoting</a>  * to overload the default value which is {@link ExchangePattern#InOut} for request/reply if no annotations are used.  *  * There are abbreviation annotations like {@link InOnly} or {@link InOut} which are typically used for  * the common message exchange patterns. You could also add this annotation onto your own custom annotation to default  * the message exchange pattern when your own annotation is added to a method  *<a href="using-exchange-pattern-annotations.html">as in this example</a>.  *  * This annotation can be added to individual methods or added to a class or interface to act as a default for all methods  * within the class or interface.  *  * See the<a href="using-exchange-pattern-annotations.html">using exchange pattern annotations</a>  * for more details on how the overloading rules work.  *  * @see InOut  * @see InOnly  * @see ExchangePattern  * @see Exchange#getPattern()  *  * @version $Revision$  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Documented
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|}
argument_list|)
DECL|annotation|Pattern
specifier|public
annotation_defn|@interface
name|Pattern
block|{
comment|/**      * Specifies the exchange pattern to be used for this method      */
DECL|method|value ()
name|ExchangePattern
name|value
parameter_list|()
function_decl|;
block|}
end_annotation_defn

end_unit

