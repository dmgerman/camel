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

begin_comment
comment|/**  * Marks a method as being a one way asynchronous invocation so that if you are using some kind of  *<a href="http://activemq.apache.org/camel/spring-remoting.html">Spring Remoting</a> then the method invocation will be asynchronous.  *  * You can then either annotate specific methods as being oneway / asynchronous via  *<code>  * @OneWay  * public void myMethod() {...}  *</code>  *  * or you can say that all methods are by default asynchronous on an interface by annotating the interface  *   *<code>  * @OneWay  * public interface Foo {  *   void methodOne();  *   void methodTwo();  * }  *</code>  *  * If you wish to use some other {@link ExchangePattern} than {@link org.apache.camel.ExchangePattern#InOnly} you could use something like  *  *<code>  * @OneWay(ExchangePattern.RobustInOnly)  * public void myMethod() {...}  *</code>  *  * otherwise the following code would default to using {@link org.apache.camel.ExchangePattern#InOnly}  *  *<code>  * @OneWay  * public void myMethod() {...}  *</code>  *  * You can also use the annotation to disable the one way pattern on specific methods as follows...  *  *<code>  * @OneWay  * public interface Foo {  *   void methodOne();  *  *   @OneWay(ExchangePattern.InOut)  *   void notOneWayMethod();  * }  *  * Where the<b>notOneWayMethod</b> will not be using one way invocation while all other methods will inherit the InOut exchange pattern  *  *</code>  * @see ExchangePattern  * @see Exchange#getPattern()  *  * @version $Revision$  */
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
DECL|annotation|OneWay
specifier|public
annotation_defn|@interface
name|OneWay
block|{
comment|/**      * Allows the exact exchange pattern type to be specified though the default value of      * {@link org.apache.camel.ExchangePattern#InOnly} should be fine for most uses      */
DECL|method|value ()
DECL|field|ExchangePattern.InOnly
specifier|public
specifier|abstract
name|ExchangePattern
name|value
parameter_list|()
default|default
name|ExchangePattern
operator|.
name|InOnly
function_decl|;
block|}
end_annotation_defn

end_unit

