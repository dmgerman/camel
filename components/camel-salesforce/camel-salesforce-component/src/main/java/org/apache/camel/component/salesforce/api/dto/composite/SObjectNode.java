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
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonAnyGetter
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
name|JsonUnwrapped
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
name|XStreamConverter
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|SObjectDescription
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

begin_comment
comment|/**  * Represents one node in the SObject tree request. SObject trees  * ({@link SObjectTree}) are composed from instances of {@link SObjectNode}s.  * Each {@link SObjectNode} contains the SObject ({@link AbstractSObjectBase})  * and any child records linked to it. SObjects at root level are added to  * {@link SObjectTree} using {@link SObjectTree#addObject(AbstractSObjectBase)},  * then you can add child records on the {@link SObjectNode} returned by using  * {@link #addChild(AbstractDescribedSObjectBase)},  * {@link #addChildren(AbstractDescribedSObjectBase, AbstractDescribedSObjectBase...)}  * or {@link #addChild(String, AbstractSObjectBase)} and  * {@link #addChildren(String, AbstractSObjectBase, AbstractSObjectBase...)}.  *<p/>  * Upon submission to the Salesforce Composite API the {@link SObjectTree} and  * the {@link SObjectNode}s in it might contain errors that you need to fetch  * using {@link #getErrors()} method.  *  * @see SObjectTree  * @see RestError  */
end_comment

begin_class
annotation|@
name|XStreamAlias
argument_list|(
literal|"records"
argument_list|)
annotation|@
name|XStreamConverter
argument_list|(
name|SObjectNodeXStreamConverter
operator|.
name|class
argument_list|)
DECL|class|SObjectNode
specifier|public
specifier|final
class|class
name|SObjectNode
implements|implements
name|Serializable
block|{
DECL|field|CHILD_PARAM
specifier|private
specifier|static
specifier|final
name|String
name|CHILD_PARAM
init|=
literal|"child"
decl_stmt|;
DECL|field|SOBJECT_TYPE_PARAM
specifier|private
specifier|static
specifier|final
name|String
name|SOBJECT_TYPE_PARAM
init|=
literal|"type"
decl_stmt|;
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
name|JsonUnwrapped
DECL|field|object
specifier|final
name|AbstractSObjectBase
name|object
decl_stmt|;
DECL|field|records
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|SObjectNode
argument_list|>
argument_list|>
name|records
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|errors
specifier|private
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
decl_stmt|;
annotation|@
name|XStreamOmitField
DECL|field|referenceGenerator
specifier|private
specifier|final
name|ReferenceGenerator
name|referenceGenerator
decl_stmt|;
DECL|method|SObjectNode (final SObjectTree tree, final AbstractSObjectBase object)
name|SObjectNode
parameter_list|(
specifier|final
name|SObjectTree
name|tree
parameter_list|,
specifier|final
name|AbstractSObjectBase
name|object
parameter_list|)
block|{
name|this
argument_list|(
name|tree
operator|.
name|referenceGenerator
argument_list|,
name|typeOf
argument_list|(
name|object
argument_list|)
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
DECL|method|SObjectNode (final ReferenceGenerator referenceGenerator, final String type, final AbstractSObjectBase object)
specifier|private
name|SObjectNode
parameter_list|(
specifier|final
name|ReferenceGenerator
name|referenceGenerator
parameter_list|,
specifier|final
name|String
name|type
parameter_list|,
specifier|final
name|AbstractSObjectBase
name|object
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
literal|"ReferenceGenerator cannot be null"
argument_list|)
expr_stmt|;
name|this
operator|.
name|object
operator|=
name|requireNonNull
argument_list|(
name|object
argument_list|,
literal|"Root SObject cannot be null"
argument_list|)
expr_stmt|;
name|object
operator|.
name|getAttributes
argument_list|()
operator|.
name|setReferenceId
argument_list|(
name|referenceGenerator
operator|.
name|nextReferenceFor
argument_list|(
name|object
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|pluralOf (final AbstractDescribedSObjectBase object)
specifier|static
name|String
name|pluralOf
parameter_list|(
specifier|final
name|AbstractDescribedSObjectBase
name|object
parameter_list|)
block|{
specifier|final
name|SObjectDescription
name|description
init|=
name|object
operator|.
name|description
argument_list|()
decl_stmt|;
return|return
name|description
operator|.
name|getLabelPlural
argument_list|()
return|;
block|}
DECL|method|typeOf (final AbstractDescribedSObjectBase object)
specifier|static
name|String
name|typeOf
parameter_list|(
specifier|final
name|AbstractDescribedSObjectBase
name|object
parameter_list|)
block|{
specifier|final
name|SObjectDescription
name|description
init|=
name|object
operator|.
name|description
argument_list|()
decl_stmt|;
return|return
name|description
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|typeOf (final AbstractSObjectBase object)
specifier|static
name|String
name|typeOf
parameter_list|(
specifier|final
name|AbstractSObjectBase
name|object
parameter_list|)
block|{
return|return
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
comment|/**      * Add a described child with the metadata needed already present within it      * to the this node.      *      * @param child to add      * @return the newly created node, used in builder fashion to add more child      *         objects to it (on the next level)      */
DECL|method|addChild (final AbstractDescribedSObjectBase child)
specifier|public
name|SObjectNode
name|addChild
parameter_list|(
specifier|final
name|AbstractDescribedSObjectBase
name|child
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|child
argument_list|,
name|CHILD_PARAM
argument_list|)
expr_stmt|;
return|return
name|addChild
argument_list|(
name|pluralOf
argument_list|(
name|child
argument_list|)
argument_list|,
name|child
argument_list|)
return|;
block|}
comment|/**      * Add a child that does not contain the required metadata to the this node.      * You need to specify the plural form of the child (e.g. `Account` its      * `Accounts`).      *      * @param labelPlural plural form      * @param child to add      * @return the newly created node, used in builder fashion to add more child      *         objects to it (on the next level)      */
DECL|method|addChild (final String labelPlural, final AbstractSObjectBase child)
specifier|public
name|SObjectNode
name|addChild
parameter_list|(
specifier|final
name|String
name|labelPlural
parameter_list|,
specifier|final
name|AbstractSObjectBase
name|child
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|labelPlural
argument_list|,
literal|"labelPlural"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|child
argument_list|,
name|CHILD_PARAM
argument_list|)
expr_stmt|;
specifier|final
name|SObjectNode
name|node
init|=
operator|new
name|SObjectNode
argument_list|(
name|referenceGenerator
argument_list|,
name|typeOf
argument_list|(
name|child
argument_list|)
argument_list|,
name|child
argument_list|)
decl_stmt|;
return|return
name|addChild
argument_list|(
name|labelPlural
argument_list|,
name|node
argument_list|)
return|;
block|}
comment|/**      * Add multiple described children with the metadata needed already present      * within them to the this node..      *      * @param first first child to add      * @param others any other children to add      */
DECL|method|addChildren (final AbstractDescribedSObjectBase first, final AbstractDescribedSObjectBase... others)
specifier|public
name|void
name|addChildren
parameter_list|(
specifier|final
name|AbstractDescribedSObjectBase
name|first
parameter_list|,
specifier|final
name|AbstractDescribedSObjectBase
modifier|...
name|others
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|first
argument_list|,
literal|"first"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|others
argument_list|,
literal|"others"
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
name|pluralOf
argument_list|(
name|first
argument_list|)
argument_list|,
name|first
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|stream
argument_list|(
name|others
argument_list|)
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|addChild
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a child that does not contain the required metadata to the this node.      * You need to specify the plural form of the child (e.g. `Account` its      * `Accounts`).      *      * @param labelPlural plural form      * @param first first child to add      * @param others any other children to add      */
DECL|method|addChildren (final String labelPlural, final AbstractSObjectBase first, final AbstractSObjectBase... others)
specifier|public
name|void
name|addChildren
parameter_list|(
specifier|final
name|String
name|labelPlural
parameter_list|,
specifier|final
name|AbstractSObjectBase
name|first
parameter_list|,
specifier|final
name|AbstractSObjectBase
modifier|...
name|others
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|labelPlural
argument_list|,
literal|"labelPlural"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|first
argument_list|,
literal|"first"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|others
argument_list|,
literal|"others"
argument_list|)
expr_stmt|;
name|addChild
argument_list|(
name|labelPlural
argument_list|,
name|first
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|stream
argument_list|(
name|others
argument_list|)
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|addChild
argument_list|(
name|labelPlural
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns all children of this node (one level deep).      *      * @return children of this node      */
annotation|@
name|JsonIgnore
DECL|method|getChildNodes ()
specifier|public
name|Stream
argument_list|<
name|SObjectNode
argument_list|>
name|getChildNodes
parameter_list|()
block|{
return|return
name|records
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|List
operator|::
name|stream
argument_list|)
return|;
block|}
comment|/**      * Returns all children of this node (one level deep) of certain type (in      * plural form).      *      * @param type type of child requested in plural form (e.g for `Account` is      *            `Accounts`)      * @return children of this node of specified type      */
DECL|method|getChildNodesOfType (final String type)
specifier|public
name|Stream
argument_list|<
name|SObjectNode
argument_list|>
name|getChildNodesOfType
parameter_list|(
specifier|final
name|String
name|type
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|type
argument_list|,
name|SOBJECT_TYPE_PARAM
argument_list|)
expr_stmt|;
return|return
name|records
operator|.
name|getOrDefault
argument_list|(
name|type
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|stream
argument_list|()
return|;
block|}
comment|/**      * Returns child SObjects of this node (one level deep).      *      * @return child SObjects of this node      */
annotation|@
name|JsonIgnore
DECL|method|getChildren ()
specifier|public
name|Stream
argument_list|<
name|AbstractSObjectBase
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|records
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|List
operator|::
name|stream
argument_list|)
operator|.
name|map
argument_list|(
name|SObjectNode
operator|::
name|getObject
argument_list|)
return|;
block|}
comment|/**      * Returns child SObjects of this node (one level deep) of certain type (in      * plural form)      *      * @param type type of child requested in plural form (e.g for `Account` is      *            `Accounts`)      * @return child SObjects of this node      */
DECL|method|getChildrenOfType (final String type)
specifier|public
name|Stream
argument_list|<
name|AbstractSObjectBase
argument_list|>
name|getChildrenOfType
parameter_list|(
specifier|final
name|String
name|type
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|type
argument_list|,
name|SOBJECT_TYPE_PARAM
argument_list|)
expr_stmt|;
return|return
name|records
operator|.
name|getOrDefault
argument_list|(
name|type
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|SObjectNode
operator|::
name|getObject
argument_list|)
return|;
block|}
comment|/**      * Errors reported against this this node received in response to the      * SObject tree being submitted.      *      * @return errors for this node      */
annotation|@
name|JsonIgnore
DECL|method|getErrors ()
specifier|public
name|List
argument_list|<
name|RestError
argument_list|>
name|getErrors
parameter_list|()
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|errors
argument_list|)
operator|.
name|orElse
argument_list|(
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * SObject at this node.      *      * @return SObject      */
annotation|@
name|JsonIgnore
DECL|method|getObject ()
specifier|public
name|AbstractSObjectBase
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
comment|/**      * Are there any errors resulted from the submission on this node?      *      * @return true if there are errors      */
DECL|method|hasErrors ()
specifier|public
name|boolean
name|hasErrors
parameter_list|()
block|{
return|return
name|errors
operator|!=
literal|null
operator|&&
operator|!
name|errors
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * Size of the branch beginning with this node (number of SObjects in it).      *      * @return number of objects within this branch      */
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|1
operator|+
name|records
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|flatMapToInt
argument_list|(
name|r
lambda|->
name|r
operator|.
name|stream
argument_list|()
operator|.
name|mapToInt
argument_list|(
name|SObjectNode
operator|::
name|size
argument_list|)
argument_list|)
operator|.
name|sum
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Node<"
operator|+
name|getObjectType
argument_list|()
operator|+
literal|">"
return|;
block|}
DECL|method|addChild (final String labelPlural, final SObjectNode node)
name|SObjectNode
name|addChild
parameter_list|(
specifier|final
name|String
name|labelPlural
parameter_list|,
specifier|final
name|SObjectNode
name|node
parameter_list|)
block|{
name|List
argument_list|<
name|SObjectNode
argument_list|>
name|children
init|=
name|records
operator|.
name|get
argument_list|(
name|labelPlural
argument_list|)
decl_stmt|;
if|if
condition|(
name|children
operator|==
literal|null
condition|)
block|{
name|children
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|records
operator|.
name|put
argument_list|(
name|labelPlural
argument_list|,
name|children
argument_list|)
expr_stmt|;
block|}
name|children
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
annotation|@
name|JsonAnyGetter
DECL|method|children ()
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|SObjectNode
argument_list|>
argument_list|>
argument_list|>
name|children
parameter_list|()
block|{
return|return
name|records
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getKey
argument_list|,
name|e
lambda|->
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"records"
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|JsonIgnore
DECL|method|getObjectType ()
name|String
name|getObjectType
parameter_list|()
block|{
return|return
name|object
operator|.
name|getAttributes
argument_list|()
operator|.
name|getType
argument_list|()
return|;
block|}
DECL|method|objectTypes ()
name|Stream
argument_list|<
name|Class
argument_list|>
name|objectTypes
parameter_list|()
block|{
return|return
name|Stream
operator|.
name|concat
argument_list|(
name|Stream
operator|.
name|of
argument_list|(
operator|(
name|Class
operator|)
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|,
name|getChildNodes
argument_list|()
operator|.
name|flatMap
argument_list|(
name|SObjectNode
operator|::
name|objectTypes
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setErrors (final List<RestError> errors)
name|void
name|setErrors
parameter_list|(
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
parameter_list|)
block|{
name|this
operator|.
name|errors
operator|=
name|errors
expr_stmt|;
block|}
block|}
end_class

end_unit

