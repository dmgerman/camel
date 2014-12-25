begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|List
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
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|Filer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|RoundEnvironment
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|SupportedAnnotationTypes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|SupportedSourceVersion
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|SourceVersion
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|ElementKind
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|TypeElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|VariableElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|type
operator|.
name|TypeMirror
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|util
operator|.
name|ElementFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|util
operator|.
name|Elements
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|FileObject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|StandardLocation
import|;
end_import

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
name|XmlAttribute
import|;
end_import

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
name|XmlElement
import|;
end_import

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
name|XmlRootElement
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|JsonSchemaHelper
operator|.
name|sanitizeDescription
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|Strings
operator|.
name|canonicalClassName
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|Strings
operator|.
name|isNullOrEmpty
import|;
end_import

begin_comment
comment|// TODO: add support for @XmlElement @XmlElementRef
end_comment

begin_class
annotation|@
name|SupportedAnnotationTypes
argument_list|(
block|{
literal|"javax.xml.bind.annotation.*"
block|}
argument_list|)
annotation|@
name|SupportedSourceVersion
argument_list|(
name|SourceVersion
operator|.
name|RELEASE_7
argument_list|)
DECL|class|ModelDocumentationAnnotationProcessor
specifier|public
class|class
name|ModelDocumentationAnnotationProcessor
extends|extends
name|AbstractAnnotationProcessor
block|{
annotation|@
name|Override
DECL|method|process (Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
specifier|public
name|boolean
name|process
parameter_list|(
name|Set
argument_list|<
name|?
extends|extends
name|TypeElement
argument_list|>
name|annotations
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|)
block|{
if|if
condition|(
name|roundEnv
operator|.
name|processingOver
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Set
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|elements
init|=
name|roundEnv
operator|.
name|getElementsAnnotatedWith
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|elements
control|)
block|{
if|if
condition|(
name|element
operator|instanceof
name|TypeElement
condition|)
block|{
name|processModelClass
argument_list|(
name|roundEnv
argument_list|,
operator|(
name|TypeElement
operator|)
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|processModelClass (final RoundEnvironment roundEnv, final TypeElement classElement)
specifier|protected
name|void
name|processModelClass
parameter_list|(
specifier|final
name|RoundEnvironment
name|roundEnv
parameter_list|,
specifier|final
name|TypeElement
name|classElement
parameter_list|)
block|{
comment|// must be from org.apache.camel.model
specifier|final
name|String
name|javaTypeName
init|=
name|canonicalClassName
argument_list|(
name|classElement
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|packageName
init|=
name|javaTypeName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|javaTypeName
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|javaTypeName
operator|.
name|startsWith
argument_list|(
literal|"org.apache.camel.model"
argument_list|)
condition|)
block|{
return|return;
block|}
specifier|final
name|XmlRootElement
name|rootElement
init|=
name|classElement
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|rootElement
operator|.
name|name
argument_list|()
decl_stmt|;
comment|// write json schema
name|String
name|fileName
init|=
name|classElement
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".json"
decl_stmt|;
name|Func1
argument_list|<
name|PrintWriter
argument_list|,
name|Void
argument_list|>
name|handler
init|=
operator|new
name|Func1
argument_list|<
name|PrintWriter
argument_list|,
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
block|{
name|writeJSonSchemeDocumentation
argument_list|(
name|writer
argument_list|,
name|roundEnv
argument_list|,
name|classElement
argument_list|,
name|rootElement
argument_list|,
name|javaTypeName
argument_list|,
name|name
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|processFile
argument_list|(
name|packageName
argument_list|,
name|fileName
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
DECL|method|writeJSonSchemeDocumentation (PrintWriter writer, RoundEnvironment roundEnv, TypeElement classElement, XmlRootElement rootElement, String javaTypeName, String name)
specifier|protected
name|void
name|writeJSonSchemeDocumentation
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|XmlRootElement
name|rootElement
parameter_list|,
name|String
name|javaTypeName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
comment|// gather eip information
name|EipModel
name|eipModel
init|=
name|findEipModelProperties
argument_list|(
name|roundEnv
argument_list|,
name|javaTypeName
argument_list|,
name|name
argument_list|)
decl_stmt|;
comment|// get endpoint information which is divided into paths and options (though there should really only be one path)
name|Set
argument_list|<
name|EipOption
argument_list|>
name|eipOptions
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|EipOption
argument_list|>
argument_list|()
decl_stmt|;
name|findClassProperties
argument_list|(
name|writer
argument_list|,
name|roundEnv
argument_list|,
name|eipOptions
argument_list|,
name|classElement
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|createParameterJsonSchema
argument_list|(
name|eipModel
argument_list|,
name|eipOptions
argument_list|)
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
DECL|method|createParameterJsonSchema (EipModel eipModel, Set<EipOption> options)
specifier|public
name|String
name|createParameterJsonSchema
parameter_list|(
name|EipModel
name|eipModel
parameter_list|,
name|Set
argument_list|<
name|EipOption
argument_list|>
name|options
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"{"
argument_list|)
decl_stmt|;
comment|// eip model
name|buffer
operator|.
name|append
argument_list|(
literal|"\n \"model\": {"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"name\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|eipModel
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"description\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|eipModel
operator|.
name|getDescription
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    \"javaType\": \""
argument_list|)
operator|.
name|append
argument_list|(
name|eipModel
operator|.
name|getJavaType
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\","
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n  },"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n  \"properties\": {"
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|EipOption
name|entry
range|:
name|options
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\n    "
argument_list|)
expr_stmt|;
comment|// as its json we need to sanitize the docs
name|String
name|doc
init|=
name|entry
operator|.
name|getDocumentationWithNotes
argument_list|()
decl_stmt|;
name|doc
operator|=
name|sanitizeDescription
argument_list|(
name|doc
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|JsonSchemaHelper
operator|.
name|toJson
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|,
name|entry
operator|.
name|getKind
argument_list|()
argument_list|,
name|entry
operator|.
name|isRequired
argument_list|()
argument_list|,
name|entry
operator|.
name|getType
argument_list|()
argument_list|,
name|entry
operator|.
name|getDefaultValue
argument_list|()
argument_list|,
name|doc
argument_list|,
name|entry
operator|.
name|isEnumType
argument_list|()
argument_list|,
name|entry
operator|.
name|getEnums
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"\n  }"
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"\n}\n"
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|findEipModelProperties (RoundEnvironment roundEnv, String javaTypeName, String name)
specifier|protected
name|EipModel
name|findEipModelProperties
parameter_list|(
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|String
name|javaTypeName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|EipModel
name|model
init|=
operator|new
name|EipModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setJavaType
argument_list|(
name|javaTypeName
argument_list|)
expr_stmt|;
name|model
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
comment|// favor to use class javadoc of component as description
if|if
condition|(
name|model
operator|.
name|getJavaType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Elements
name|elementUtils
init|=
name|processingEnv
operator|.
name|getElementUtils
argument_list|()
decl_stmt|;
name|TypeElement
name|typeElement
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|model
operator|.
name|getJavaType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeElement
operator|!=
literal|null
condition|)
block|{
name|String
name|doc
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|typeElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
comment|// need to sanitize the description first (we only want a summary)
name|doc
operator|=
name|sanitizeDescription
argument_list|(
name|doc
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// the javadoc may actually be empty, so only change the doc if we got something
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|model
operator|.
name|setDescription
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|model
return|;
block|}
DECL|method|findClassProperties (PrintWriter writer, RoundEnvironment roundEnv, Set<EipOption> eipOptions, TypeElement classElement, String prefix)
specifier|protected
name|void
name|findClassProperties
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|Set
argument_list|<
name|EipOption
argument_list|>
name|eipOptions
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|Elements
name|elementUtils
init|=
name|processingEnv
operator|.
name|getElementUtils
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|List
argument_list|<
name|VariableElement
argument_list|>
name|fieldElements
init|=
name|ElementFilter
operator|.
name|fieldsIn
argument_list|(
name|classElement
operator|.
name|getEnclosedElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|VariableElement
name|fieldElement
range|:
name|fieldElements
control|)
block|{
name|XmlAttribute
name|attribute
init|=
name|fieldElement
operator|.
name|getAnnotation
argument_list|(
name|XmlAttribute
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|fieldName
init|=
name|fieldElement
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|attribute
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|name
argument_list|)
operator|||
literal|"##default"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|fieldName
expr_stmt|;
block|}
name|name
operator|=
name|prefix
operator|+
name|name
expr_stmt|;
name|TypeMirror
name|fieldType
init|=
name|fieldElement
operator|.
name|asType
argument_list|()
decl_stmt|;
name|String
name|fieldTypeName
init|=
name|fieldType
operator|.
name|toString
argument_list|()
decl_stmt|;
name|TypeElement
name|fieldTypeElement
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|fieldTypeName
argument_list|)
decl_stmt|;
name|String
name|docComment
init|=
name|findJavaDoc
argument_list|(
name|elementUtils
argument_list|,
name|fieldElement
argument_list|,
name|fieldName
argument_list|,
name|classElement
argument_list|)
decl_stmt|;
name|boolean
name|required
init|=
name|attribute
operator|.
name|required
argument_list|()
decl_stmt|;
comment|// gather enums
name|Set
argument_list|<
name|String
argument_list|>
name|enums
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|isEnum
init|=
name|fieldTypeElement
operator|!=
literal|null
operator|&&
name|fieldTypeElement
operator|.
name|getKind
argument_list|()
operator|==
name|ElementKind
operator|.
name|ENUM
decl_stmt|;
if|if
condition|(
name|isEnum
condition|)
block|{
name|TypeElement
name|enumClass
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|fieldTypeElement
operator|.
name|asType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// find all the enum constants which has the possible enum value that can be used
name|List
argument_list|<
name|VariableElement
argument_list|>
name|fields
init|=
name|ElementFilter
operator|.
name|fieldsIn
argument_list|(
name|enumClass
operator|.
name|getEnclosedElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|VariableElement
name|var
range|:
name|fields
control|)
block|{
if|if
condition|(
name|var
operator|.
name|getKind
argument_list|()
operator|==
name|ElementKind
operator|.
name|ENUM_CONSTANT
condition|)
block|{
name|String
name|val
init|=
name|var
operator|.
name|toString
argument_list|()
decl_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|EipOption
name|ep
init|=
operator|new
name|EipOption
argument_list|(
name|name
argument_list|,
literal|"attribute"
argument_list|,
name|fieldTypeName
argument_list|,
name|required
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
name|docComment
argument_list|,
name|isEnum
argument_list|,
name|enums
argument_list|)
decl_stmt|;
name|eipOptions
operator|.
name|add
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
name|XmlElement
name|element
init|=
name|fieldElement
operator|.
name|getAnnotation
argument_list|(
name|XmlElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|fieldName
operator|=
name|fieldElement
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|element
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|name
argument_list|)
operator|||
literal|"##default"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|fieldName
expr_stmt|;
block|}
name|name
operator|=
name|prefix
operator|+
name|name
expr_stmt|;
name|TypeMirror
name|fieldType
init|=
name|fieldElement
operator|.
name|asType
argument_list|()
decl_stmt|;
name|String
name|fieldTypeName
init|=
name|fieldType
operator|.
name|toString
argument_list|()
decl_stmt|;
name|TypeElement
name|fieldTypeElement
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|fieldTypeName
argument_list|)
decl_stmt|;
name|String
name|docComment
init|=
name|findJavaDoc
argument_list|(
name|elementUtils
argument_list|,
name|fieldElement
argument_list|,
name|fieldName
argument_list|,
name|classElement
argument_list|)
decl_stmt|;
name|boolean
name|required
init|=
name|element
operator|.
name|required
argument_list|()
decl_stmt|;
comment|// gather enums
name|Set
argument_list|<
name|String
argument_list|>
name|enums
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|boolean
name|isEnum
init|=
name|fieldTypeElement
operator|!=
literal|null
operator|&&
name|fieldTypeElement
operator|.
name|getKind
argument_list|()
operator|==
name|ElementKind
operator|.
name|ENUM
decl_stmt|;
if|if
condition|(
name|isEnum
condition|)
block|{
name|TypeElement
name|enumClass
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|fieldTypeElement
operator|.
name|asType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// find all the enum constants which has the possible enum value that can be used
name|List
argument_list|<
name|VariableElement
argument_list|>
name|fields
init|=
name|ElementFilter
operator|.
name|fieldsIn
argument_list|(
name|enumClass
operator|.
name|getEnclosedElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|VariableElement
name|var
range|:
name|fields
control|)
block|{
if|if
condition|(
name|var
operator|.
name|getKind
argument_list|()
operator|==
name|ElementKind
operator|.
name|ENUM_CONSTANT
condition|)
block|{
name|String
name|val
init|=
name|var
operator|.
name|toString
argument_list|()
decl_stmt|;
name|enums
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|EipOption
name|ep
init|=
operator|new
name|EipOption
argument_list|(
name|name
argument_list|,
literal|"element"
argument_list|,
name|fieldTypeName
argument_list|,
name|required
argument_list|,
literal|""
argument_list|,
literal|""
argument_list|,
name|docComment
argument_list|,
name|isEnum
argument_list|,
name|enums
argument_list|)
decl_stmt|;
name|eipOptions
operator|.
name|add
argument_list|(
name|ep
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check super classes which may also have @UriParam fields
name|TypeElement
name|baseTypeElement
init|=
literal|null
decl_stmt|;
name|TypeMirror
name|superclass
init|=
name|classElement
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superclass
operator|!=
literal|null
condition|)
block|{
name|String
name|superClassName
init|=
name|canonicalClassName
argument_list|(
name|superclass
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|baseTypeElement
operator|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|superClassName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|baseTypeElement
operator|!=
literal|null
condition|)
block|{
name|classElement
operator|=
name|baseTypeElement
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
block|}
DECL|class|EipModel
specifier|private
specifier|static
specifier|final
class|class
name|EipModel
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|javaType
specifier|private
name|String
name|javaType
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getJavaType ()
specifier|public
name|String
name|getJavaType
parameter_list|()
block|{
return|return
name|javaType
return|;
block|}
DECL|method|setJavaType (String javaType)
specifier|public
name|void
name|setJavaType
parameter_list|(
name|String
name|javaType
parameter_list|)
block|{
name|this
operator|.
name|javaType
operator|=
name|javaType
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
block|}
DECL|class|EipOption
specifier|private
specifier|static
specifier|final
class|class
name|EipOption
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|kind
specifier|private
name|String
name|kind
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|required
specifier|private
name|boolean
name|required
decl_stmt|;
DECL|field|defaultValue
specifier|private
name|String
name|defaultValue
decl_stmt|;
DECL|field|defaultValueNote
specifier|private
name|String
name|defaultValueNote
decl_stmt|;
DECL|field|documentation
specifier|private
name|String
name|documentation
decl_stmt|;
DECL|field|enumType
specifier|private
name|boolean
name|enumType
decl_stmt|;
DECL|field|enums
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|enums
decl_stmt|;
DECL|method|EipOption (String name, String kind, String type, boolean required, String defaultValue, String defaultValueNote, String documentation, boolean enumType, Set<String> enums)
specifier|private
name|EipOption
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|kind
parameter_list|,
name|String
name|type
parameter_list|,
name|boolean
name|required
parameter_list|,
name|String
name|defaultValue
parameter_list|,
name|String
name|defaultValueNote
parameter_list|,
name|String
name|documentation
parameter_list|,
name|boolean
name|enumType
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|enums
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|kind
operator|=
name|kind
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
name|this
operator|.
name|defaultValue
operator|=
name|defaultValue
expr_stmt|;
name|this
operator|.
name|defaultValueNote
operator|=
name|defaultValueNote
expr_stmt|;
name|this
operator|.
name|documentation
operator|=
name|documentation
expr_stmt|;
name|this
operator|.
name|enumType
operator|=
name|enumType
expr_stmt|;
name|this
operator|.
name|enums
operator|=
name|enums
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getKind ()
specifier|public
name|String
name|getKind
parameter_list|()
block|{
return|return
name|kind
return|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|isRequired ()
specifier|public
name|boolean
name|isRequired
parameter_list|()
block|{
return|return
name|required
return|;
block|}
DECL|method|getDefaultValue ()
specifier|public
name|String
name|getDefaultValue
parameter_list|()
block|{
return|return
name|defaultValue
return|;
block|}
DECL|method|getDocumentation ()
specifier|public
name|String
name|getDocumentation
parameter_list|()
block|{
return|return
name|documentation
return|;
block|}
DECL|method|getEnumValuesAsHtml ()
specifier|public
name|String
name|getEnumValuesAsHtml
parameter_list|()
block|{
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|"<br/>"
argument_list|)
decl_stmt|;
if|if
condition|(
name|enums
operator|!=
literal|null
operator|&&
name|enums
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|e
range|:
name|enums
control|)
block|{
name|csb
operator|.
name|append
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|csb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getDocumentationWithNotes ()
specifier|public
name|String
name|getDocumentationWithNotes
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|documentation
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|defaultValueNote
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|". Default value notice: "
argument_list|)
operator|.
name|append
argument_list|(
name|defaultValueNote
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isEnumType ()
specifier|public
name|boolean
name|isEnumType
parameter_list|()
block|{
return|return
name|enumType
return|;
block|}
DECL|method|getEnums ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEnums
parameter_list|()
block|{
return|return
name|enums
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EipOption
name|that
init|=
operator|(
name|EipOption
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

