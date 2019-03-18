begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.dto.composite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|composite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|BeanInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|IntrospectionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|Introspector
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyDescriptor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|Set
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
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|StreamSupport
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnore
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonProperty
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamAlias
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamImplicit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|annotations
operator|.
name|XStreamOmitField
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AbstractDescribedSObjectBase
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|AbstractSObjectBase
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|RestError
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

begin_comment
comment|/**  * Payload and response for the SObject tree Composite API. The main interface for specifying what to include in the  * sumission to the API endpoint. To build the tree out use:<blockquote>  *  *<pre>  * {@code  * Account account = ...  * Contact president = ...  * Contact marketing = ...  *  * Account anotherAccount = ...  * Contact sales = ...  * Asset someAsset = ...  *  * SObjectTree request = new SObjectTree();  * request.addObject(account).addChildren(president, marketing);  * request.addObject(anotherAccount).addChild(sales).addChild(someAsset);  * }  *</pre>  *  *</blockquote>  *  * This will generate a tree of SObjects resembling:<blockquote>  *  *<pre>  * .  * |-- account  * |   |-- president  * |   `-- marketing  * `-- anotherAccount  *     `-- sales  *         `-- someAsset  *</pre>  *  *</blockquote>  *  * By default references that correlate between SObjects in the tree and returned identifiers and errors are handled  * automatically, if you wish to customize the generation of the reference implement {@link ReferenceGenerator} and  * supply it as constructor argument to {@link #SObjectTree(ReferenceGenerator)}.  *<p/>  * Note that the tree can hold single object type at the root of the tree.  *  * @see ReferenceGenerator  * @see SObjectNode  * @see AbstractSObjectBase  * @see AbstractDescribedSObjectBase  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"SObjectTreeRequest"
argument_list|)
DECL|class|SObjectTree
specifier|public
specifier|final
class|class
name|SObjectTree
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|XStreamImplicit
annotation|@
name|JsonProperty
DECL|field|records
specifier|final
name|List
argument_list|<
name|SObjectNode
argument_list|>
name|records
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XStreamOmitField
DECL|field|referenceGenerator
specifier|final
name|ReferenceGenerator
name|referenceGenerator
decl_stmt|;
annotation|@
name|XStreamOmitField
DECL|field|objectType
specifier|private
name|String
name|objectType
decl_stmt|;
comment|/**      * Create new SObject tree with the default {@link ReferenceGenerator}.      */
DECL|method|SObjectTree ()
specifier|public
name|SObjectTree
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|Counter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create new SObject tree with custom {@link ReferenceGenerator}.      */
DECL|method|SObjectTree (final ReferenceGenerator referenceGenerator)
specifier|public
name|SObjectTree
parameter_list|(
specifier|final
name|ReferenceGenerator
name|referenceGenerator
parameter_list|)
block|{
name|this
operator|.
name|referenceGenerator
operator|=
name|requireNonNull
argument_list|(
name|referenceGenerator
argument_list|,
literal|"You must specify the ReferenceGenerator implementation"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add SObject at the root of the tree.      *      * @param object      *            SObject to add      * @return {@link SObjectNode} for the given SObject      */
DECL|method|addObject (final AbstractSObjectBase object)
specifier|public
name|SObjectNode
name|addObject
parameter_list|(
specifier|final
name|AbstractSObjectBase
name|object
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|object
argument_list|,
literal|"object"
argument_list|)
expr_stmt|;
return|return
name|addNode
argument_list|(
operator|new
name|SObjectNode
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns a stream of all nodes in the tree.      *      * @return      */
DECL|method|allNodes ()
specifier|public
name|Stream
argument_list|<
name|SObjectNode
argument_list|>
name|allNodes
parameter_list|()
block|{
return|return
name|records
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|r
lambda|->
name|Stream
operator|.
name|concat
argument_list|(
name|Stream
operator|.
name|of
argument_list|(
name|r
argument_list|)
argument_list|,
name|r
operator|.
name|getChildNodes
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns a stream of all objects in the tree.      *      * @return      */
DECL|method|allObjects ()
specifier|public
name|Stream
argument_list|<
name|AbstractSObjectBase
argument_list|>
name|allObjects
parameter_list|()
block|{
return|return
name|records
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|r
lambda|->
name|Stream
operator|.
name|concat
argument_list|(
name|Stream
operator|.
name|of
argument_list|(
name|r
operator|.
name|getObject
argument_list|()
argument_list|)
argument_list|,
name|r
operator|.
name|getChildren
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns the type of the objects in the root of the tree.      *      * @return object type      */
annotation|@
name|JsonIgnore
DECL|method|getObjectType ()
specifier|public
name|String
name|getObjectType
parameter_list|()
block|{
return|return
name|objectType
return|;
block|}
DECL|method|objectTypes ()
specifier|public
name|Class
index|[]
name|objectTypes
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|Class
argument_list|>
name|types
init|=
name|records
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|n
lambda|->
name|n
operator|.
name|objectTypes
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|types
operator|.
name|toArray
argument_list|(
operator|new
name|Class
index|[
name|types
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Sets errors for the given reference. Used when processing the response of API invocation.      *      * @param referenceId      *            reference identifier      * @param errors      *            list of {@link RestError}      */
DECL|method|setErrorFor (final String referenceId, final List<RestError> errors)
specifier|public
name|void
name|setErrorFor
parameter_list|(
specifier|final
name|String
name|referenceId
parameter_list|,
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|)
block|{
for|for
control|(
specifier|final
name|SObjectNode
name|node
range|:
name|records
control|)
block|{
if|if
condition|(
name|setErrorFor
argument_list|(
name|node
argument_list|,
name|referenceId
argument_list|,
name|errors
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
block|}
comment|/**      * Sets identifier of SObject for the given reference. Used when processing the response of API invocation.      *      * @param referenceId      *            reference identifier      * @param id      *            SObject identifier      */
DECL|method|setIdFor (final String referenceId, final String id)
specifier|public
name|void
name|setIdFor
parameter_list|(
specifier|final
name|String
name|referenceId
parameter_list|,
specifier|final
name|String
name|id
parameter_list|)
block|{
for|for
control|(
specifier|final
name|SObjectNode
name|node
range|:
name|records
control|)
block|{
if|if
condition|(
name|setIdFor
argument_list|(
name|node
argument_list|,
name|referenceId
argument_list|,
name|id
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
block|}
comment|/**      * Returns the number of elements in the tree.      *      * @return number of elements in the tree      */
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|records
operator|.
name|stream
argument_list|()
operator|.
name|mapToInt
argument_list|(
name|r
lambda|->
name|r
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|sum
argument_list|()
return|;
block|}
DECL|method|addNode (final SObjectNode node)
name|SObjectNode
name|addNode
parameter_list|(
specifier|final
name|SObjectNode
name|node
parameter_list|)
block|{
specifier|final
name|String
name|givenObjectType
init|=
name|node
operator|.
name|getObjectType
argument_list|()
decl_stmt|;
if|if
condition|(
name|objectType
operator|!=
literal|null
operator|&&
operator|!
name|objectType
operator|.
name|equals
argument_list|(
name|givenObjectType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"SObjectTree can hold only records of the same type, previously given: "
operator|+
name|objectType
operator|+
literal|", and now trying to add: "
operator|+
name|givenObjectType
argument_list|)
throw|;
block|}
name|objectType
operator|=
name|givenObjectType
expr_stmt|;
name|records
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
DECL|method|setErrorFor (final SObjectNode node, final String referenceId, final List<RestError> errors)
name|boolean
name|setErrorFor
parameter_list|(
specifier|final
name|SObjectNode
name|node
parameter_list|,
specifier|final
name|String
name|referenceId
parameter_list|,
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|)
block|{
specifier|final
name|Attributes
name|attributes
init|=
name|node
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
specifier|final
name|String
name|attributesReferenceId
init|=
name|attributes
operator|.
name|getReferenceId
argument_list|()
decl_stmt|;
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|attributesReferenceId
argument_list|,
name|referenceId
argument_list|)
condition|)
block|{
name|node
operator|.
name|setErrors
argument_list|(
name|errors
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
name|StreamSupport
operator|.
name|stream
argument_list|(
name|node
operator|.
name|getChildNodes
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|n
lambda|->
name|setErrorFor
argument_list|(
name|n
argument_list|,
name|referenceId
argument_list|,
name|errors
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setIdFor (final SObjectNode node, final String referenceId, final String id)
name|boolean
name|setIdFor
parameter_list|(
specifier|final
name|SObjectNode
name|node
parameter_list|,
specifier|final
name|String
name|referenceId
parameter_list|,
specifier|final
name|String
name|id
parameter_list|)
block|{
specifier|final
name|Attributes
name|attributes
init|=
name|node
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
specifier|final
name|String
name|attributesReferenceId
init|=
name|attributes
operator|.
name|getReferenceId
argument_list|()
decl_stmt|;
if|if
condition|(
name|Objects
operator|.
name|equals
argument_list|(
name|attributesReferenceId
argument_list|,
name|referenceId
argument_list|)
condition|)
block|{
specifier|final
name|Object
name|object
init|=
name|node
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
return|return
name|updateBaseObjectId
argument_list|(
name|id
argument_list|,
operator|(
name|AbstractSObjectBase
operator|)
name|object
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|updateGeneralObjectId
argument_list|(
name|id
argument_list|,
name|object
argument_list|)
return|;
block|}
block|}
return|return
name|StreamSupport
operator|.
name|stream
argument_list|(
name|node
operator|.
name|getChildNodes
argument_list|()
operator|.
name|spliterator
argument_list|()
argument_list|,
literal|false
argument_list|)
operator|.
name|anyMatch
argument_list|(
name|n
lambda|->
name|setIdFor
argument_list|(
name|n
argument_list|,
name|referenceId
argument_list|,
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|updateBaseObjectId (final String id, final AbstractSObjectBase object)
name|boolean
name|updateBaseObjectId
parameter_list|(
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|AbstractSObjectBase
name|object
parameter_list|)
block|{
name|object
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|updateGeneralObjectId (final String id, final Object object)
name|boolean
name|updateGeneralObjectId
parameter_list|(
specifier|final
name|String
name|id
parameter_list|,
specifier|final
name|Object
name|object
parameter_list|)
block|{
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
name|clazz
init|=
name|object
operator|.
name|getClass
argument_list|()
decl_stmt|;
specifier|final
name|BeanInfo
name|beanInfo
decl_stmt|;
try|try
block|{
name|beanInfo
operator|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IntrospectionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
specifier|final
name|PropertyDescriptor
index|[]
name|propertyDescriptors
init|=
name|beanInfo
operator|.
name|getPropertyDescriptors
argument_list|()
decl_stmt|;
specifier|final
name|Optional
argument_list|<
name|PropertyDescriptor
argument_list|>
name|maybeIdProperty
init|=
name|Arrays
operator|.
name|stream
argument_list|(
name|propertyDescriptors
argument_list|)
operator|.
name|filter
argument_list|(
name|pd
lambda|->
literal|"id"
operator|.
name|equals
argument_list|(
name|pd
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
decl_stmt|;
if|if
condition|(
name|maybeIdProperty
operator|.
name|isPresent
argument_list|()
condition|)
block|{
specifier|final
name|Method
name|readMethod
init|=
name|maybeIdProperty
operator|.
name|get
argument_list|()
operator|.
name|getReadMethod
argument_list|()
decl_stmt|;
try|try
block|{
name|readMethod
operator|.
name|invoke
argument_list|(
name|object
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

