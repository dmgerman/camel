begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer.example.abc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
operator|.
name|example
operator|.
name|abc
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRegistry
import|;
end_import

begin_comment
comment|/**  * This object contains factory methods for each   * Java content interface and Java element interface   * generated in the example.abc package.   *<p>An ObjectFactory allows you to programatically   * construct new instances of the Java representation   * for XML content. The Java representation of XML   * content can consist of schema derived interfaces   * and classes representing the binding of schema   * type definitions, element declarations and model   * groups.  Factory methods for each of these are   * provided in this class.  */
end_comment

begin_class
annotation|@
name|XmlRegistry
DECL|class|ObjectFactory
specifier|public
class|class
name|ObjectFactory
block|{
comment|/**      * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: example.abc      *       */
DECL|method|ObjectFactory ()
specifier|public
name|ObjectFactory
parameter_list|()
block|{     }
comment|/**      * Create an instance of {@link ABCOrder }      *       */
DECL|method|createABCOrder ()
specifier|public
name|ABCOrder
name|createABCOrder
parameter_list|()
block|{
return|return
operator|new
name|ABCOrder
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link ABCOrder.OrderItems }      *       */
DECL|method|createABCOrderOrderItems ()
specifier|public
name|ABCOrder
operator|.
name|OrderItems
name|createABCOrderOrderItems
parameter_list|()
block|{
return|return
operator|new
name|ABCOrder
operator|.
name|OrderItems
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link ABCOrder.Header }      *       */
DECL|method|createABCOrderHeader ()
specifier|public
name|ABCOrder
operator|.
name|Header
name|createABCOrderHeader
parameter_list|()
block|{
return|return
operator|new
name|ABCOrder
operator|.
name|Header
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link ABCOrder.OrderItems.Item }      *       */
DECL|method|createABCOrderOrderItemsItem ()
specifier|public
name|ABCOrder
operator|.
name|OrderItems
operator|.
name|Item
name|createABCOrderOrderItemsItem
parameter_list|()
block|{
return|return
operator|new
name|ABCOrder
operator|.
name|OrderItems
operator|.
name|Item
argument_list|()
return|;
block|}
block|}
end_class

end_unit

