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
name|Documented
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

begin_comment
comment|/**  * Marks a method as being {@link ExchangePattern#InOut} when a class or interface has been annotated with  * {@link InOnly} when using  *<a href="http://camel.apache.org/bean-integration.html">Bean Integration</a> or  *<a href="http://camel.apache.org/spring-remoting.html">Spring Remoting</a>.  *  * This annotation is only intended to be used on methods which the class or interface has been annotated with  * a default exchange pattern annotation such as {@link InOnly} or {@link Pattern}  *  * See the<a href="using-exchange-pattern-annotations.html">using exchange pattern annotations</a>  * for more details on how the overloading rules work.  *  * @see org.apache.camel.ExchangePattern  * @see org.apache.camel.Exchange#getPattern()  * @see InOnly  * @see Pattern  */
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
name|METHOD
block|}
argument_list|)
annotation|@
name|Pattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
DECL|annotation|InOut
specifier|public
annotation_defn|@interface
name|InOut
block|{ }
end_annotation_defn

end_unit

