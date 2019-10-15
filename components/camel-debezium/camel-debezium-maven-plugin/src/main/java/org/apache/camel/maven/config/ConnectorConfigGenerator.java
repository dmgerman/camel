begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
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
name|Map
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
name|io
operator|.
name|debezium
operator|.
name|config
operator|.
name|CommonConnectorConfig
import|;
end_import

begin_import
import|import
name|io
operator|.
name|debezium
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|io
operator|.
name|debezium
operator|.
name|config
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|io
operator|.
name|debezium
operator|.
name|relational
operator|.
name|history
operator|.
name|FileDatabaseHistory
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
name|debezium
operator|.
name|configuration
operator|.
name|ConfigurationValidation
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
name|maven
operator|.
name|packaging
operator|.
name|srcgen
operator|.
name|Annotation
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
name|maven
operator|.
name|packaging
operator|.
name|srcgen
operator|.
name|JavaClass
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
name|maven
operator|.
name|packaging
operator|.
name|srcgen
operator|.
name|Method
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|apache
operator|.
name|kafka
operator|.
name|common
operator|.
name|config
operator|.
name|ConfigDef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|source
operator|.
name|SourceConnector
import|;
end_import

begin_class
DECL|class|ConnectorConfigGenerator
specifier|public
specifier|final
class|class
name|ConnectorConfigGenerator
block|{
DECL|field|PACKAGE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|PACKAGE_NAME
init|=
literal|"org.apache.camel.component.debezium.configuration"
decl_stmt|;
DECL|field|PARENT_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|PARENT_TYPE
init|=
literal|"EmbeddedDebeziumConfiguration"
decl_stmt|;
DECL|field|connector
specifier|private
specifier|final
name|SourceConnector
name|connector
decl_stmt|;
DECL|field|dbzConfigFields
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ConnectorConfigField
argument_list|>
name|dbzConfigFields
decl_stmt|;
DECL|field|connectorName
specifier|private
specifier|final
name|String
name|connectorName
decl_stmt|;
DECL|field|className
specifier|private
specifier|final
name|String
name|className
decl_stmt|;
DECL|field|javaClass
specifier|private
specifier|final
name|JavaClass
name|javaClass
init|=
operator|new
name|JavaClass
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|ConnectorConfigGenerator (final SourceConnector connector, final Map<String, ConnectorConfigField> dbzConfigFields, final String connectorName)
specifier|private
name|ConnectorConfigGenerator
parameter_list|(
specifier|final
name|SourceConnector
name|connector
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ConnectorConfigField
argument_list|>
name|dbzConfigFields
parameter_list|,
specifier|final
name|String
name|connectorName
parameter_list|)
block|{
name|this
operator|.
name|connector
operator|=
name|connector
expr_stmt|;
name|this
operator|.
name|dbzConfigFields
operator|=
name|dbzConfigFields
expr_stmt|;
name|this
operator|.
name|connectorName
operator|=
name|connectorName
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|connectorName
operator|+
literal|"Connector"
operator|+
name|PARENT_TYPE
expr_stmt|;
comment|// generate our java class
name|generateJavaClass
argument_list|()
expr_stmt|;
block|}
DECL|method|create (final SourceConnector connector, final Class<?> dbzConfigClass)
specifier|public
specifier|static
name|ConnectorConfigGenerator
name|create
parameter_list|(
specifier|final
name|SourceConnector
name|connector
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|dbzConfigClass
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|connector
argument_list|,
name|dbzConfigClass
argument_list|,
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
return|;
block|}
DECL|method|create (final SourceConnector connector, final Class<?> dbzConfigClass, final Set<String> requiredFields)
specifier|public
specifier|static
name|ConnectorConfigGenerator
name|create
parameter_list|(
specifier|final
name|SourceConnector
name|connector
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|dbzConfigClass
parameter_list|,
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|requiredFields
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|connector
argument_list|,
name|dbzConfigClass
argument_list|,
name|requiredFields
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
return|;
block|}
DECL|method|create (final SourceConnector connector, final Class<?> dbzConfigClass, final Map<String, Object> overridenDefaultValues)
specifier|public
specifier|static
name|ConnectorConfigGenerator
name|create
parameter_list|(
specifier|final
name|SourceConnector
name|connector
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|dbzConfigClass
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|overridenDefaultValues
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|connector
argument_list|,
name|dbzConfigClass
argument_list|,
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|overridenDefaultValues
argument_list|)
return|;
block|}
DECL|method|create (final SourceConnector connector, final Class<?> dbzConfigClass, final Set<String> requiredFields, final Map<String, Object> overridenDefaultValues)
specifier|public
specifier|static
name|ConnectorConfigGenerator
name|create
parameter_list|(
specifier|final
name|SourceConnector
name|connector
parameter_list|,
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|dbzConfigClass
parameter_list|,
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|requiredFields
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|overridenDefaultValues
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|connector
argument_list|,
literal|"connector"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|dbzConfigClass
argument_list|,
literal|"dbzConfigClass"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|requiredFields
argument_list|,
literal|"requiredFields"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|overridenDefaultValues
argument_list|,
literal|"overridenDefaultValues"
argument_list|)
expr_stmt|;
comment|// check if config class is correct
if|if
condition|(
operator|!
name|isConfigClassValid
argument_list|(
name|dbzConfigClass
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Class '%s' is not valid Debezium configuration class"
argument_list|,
name|dbzConfigClass
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
specifier|final
name|ConfigDef
name|configDef
init|=
name|connector
operator|.
name|config
argument_list|()
decl_stmt|;
comment|// add additional fields
name|Field
operator|.
name|group
argument_list|(
name|configDef
argument_list|,
literal|"additionalFields"
argument_list|,
name|FileDatabaseHistory
operator|.
name|FILE_PATH
argument_list|)
expr_stmt|;
comment|// get the name of the connector from the configClass
specifier|final
name|String
name|connectorName
init|=
name|dbzConfigClass
operator|.
name|getSimpleName
argument_list|()
operator|.
name|replace
argument_list|(
literal|"ConnectorConfig"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
return|return
operator|new
name|ConnectorConfigGenerator
argument_list|(
name|connector
argument_list|,
name|ConnectorConfigFieldsFactory
operator|.
name|createConnectorFieldsAsMap
argument_list|(
name|configDef
argument_list|,
name|dbzConfigClass
argument_list|,
name|requiredFields
argument_list|,
name|overridenDefaultValues
argument_list|)
argument_list|,
name|connectorName
argument_list|)
return|;
block|}
DECL|method|getConnectorName ()
specifier|public
name|String
name|getConnectorName
parameter_list|()
block|{
return|return
name|connectorName
return|;
block|}
DECL|method|getClassName ()
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
DECL|method|getPackageName ()
specifier|public
name|String
name|getPackageName
parameter_list|()
block|{
return|return
name|PACKAGE_NAME
return|;
block|}
DECL|method|printGeneratedClass (final OutputStream outputStream)
specifier|public
name|void
name|printGeneratedClass
parameter_list|(
specifier|final
name|OutputStream
name|outputStream
parameter_list|)
block|{
specifier|final
name|PrintStream
name|printStreams
init|=
operator|new
name|PrintStream
argument_list|(
name|outputStream
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|printStreams
operator|.
name|println
argument_list|(
name|printClassAsString
argument_list|()
argument_list|)
expr_stmt|;
name|printStreams
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|printClassAsString ()
specifier|public
name|String
name|printClassAsString
parameter_list|()
block|{
return|return
name|javaClass
operator|.
name|printClass
argument_list|(
literal|true
argument_list|)
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
name|printClassAsString
argument_list|()
return|;
block|}
DECL|method|isConfigClassValid (final Class<?> configClass)
specifier|private
specifier|static
name|boolean
name|isConfigClassValid
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|configClass
parameter_list|)
block|{
comment|// config class should be a subtype of CommonConnectorConfig
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|configClass
decl_stmt|;
while|while
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|clazz
operator|==
name|CommonConnectorConfig
operator|.
name|class
condition|)
block|{
return|return
literal|true
return|;
block|}
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|generateJavaClass ()
specifier|private
name|void
name|generateJavaClass
parameter_list|()
block|{
name|setPackage
argument_list|()
expr_stmt|;
name|setImports
argument_list|()
expr_stmt|;
name|setClassNameAndType
argument_list|()
expr_stmt|;
name|setClassFields
argument_list|()
expr_stmt|;
name|setSettersAndGettersMethods
argument_list|()
expr_stmt|;
name|setCreateConnectorConfigurationMethod
argument_list|()
expr_stmt|;
name|setConfigureConnectorClassMethod
argument_list|()
expr_stmt|;
name|setValidateConnectorConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|setPackage ()
specifier|private
name|void
name|setPackage
parameter_list|()
block|{
name|javaClass
operator|.
name|setPackage
argument_list|(
name|PACKAGE_NAME
argument_list|)
expr_stmt|;
block|}
DECL|method|setImports ()
specifier|private
name|void
name|setImports
parameter_list|()
block|{
name|javaClass
operator|.
name|addImport
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
expr_stmt|;
name|javaClass
operator|.
name|addImport
argument_list|(
name|connector
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|javaClass
operator|.
name|addImport
argument_list|(
name|Metadata
operator|.
name|class
argument_list|)
expr_stmt|;
name|javaClass
operator|.
name|addImport
argument_list|(
name|UriParam
operator|.
name|class
argument_list|)
expr_stmt|;
name|javaClass
operator|.
name|addImport
argument_list|(
name|UriParams
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|setClassNameAndType ()
specifier|private
name|void
name|setClassNameAndType
parameter_list|()
block|{
name|javaClass
operator|.
name|setName
argument_list|(
name|className
argument_list|)
operator|.
name|extendSuperType
argument_list|(
name|PARENT_TYPE
argument_list|)
operator|.
name|addAnnotation
argument_list|(
name|UriParams
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|setClassFields ()
specifier|private
name|void
name|setClassFields
parameter_list|()
block|{
comment|// String LABEL_NAME
name|javaClass
operator|.
name|addField
argument_list|()
operator|.
name|setName
argument_list|(
literal|"LABEL_NAME"
argument_list|)
operator|.
name|setFinal
argument_list|(
literal|true
argument_list|)
operator|.
name|setStatic
argument_list|(
literal|true
argument_list|)
operator|.
name|setPrivate
argument_list|()
operator|.
name|setType
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|setLiteralInitializer
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"\"consumer,%s\""
argument_list|,
name|connectorName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// connector fields
name|dbzConfigFields
operator|.
name|forEach
argument_list|(
parameter_list|(
name|fieldName
parameter_list|,
name|fieldConfig
parameter_list|)
lambda|->
block|{
if|if
condition|(
operator|!
name|isFieldInternalOrDeprecated
argument_list|(
name|fieldConfig
argument_list|)
condition|)
block|{
specifier|final
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
operator|.
name|packaging
operator|.
name|srcgen
operator|.
name|Field
name|field
init|=
name|javaClass
operator|.
name|addField
argument_list|()
operator|.
name|setName
argument_list|(
name|fieldConfig
operator|.
name|getFieldName
argument_list|()
argument_list|)
operator|.
name|setType
argument_list|(
name|fieldConfig
operator|.
name|getRawType
argument_list|()
argument_list|)
operator|.
name|setPrivate
argument_list|()
operator|.
name|setLiteralInitializer
argument_list|(
name|fieldConfig
operator|.
name|getDefaultValueAsString
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|description
init|=
name|fieldConfig
operator|.
name|getDescription
argument_list|()
decl_stmt|;
if|if
condition|(
name|description
operator|==
literal|null
operator|||
name|description
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|description
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Description is not available here, please check Debezium website for corresponding key '%s' description."
argument_list|,
name|fieldName
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|getJavaDoc
argument_list|()
operator|.
name|setText
argument_list|(
name|description
argument_list|)
expr_stmt|;
specifier|final
name|Annotation
name|annotation
init|=
name|field
operator|.
name|addAnnotation
argument_list|(
name|UriParam
operator|.
name|class
argument_list|)
operator|.
name|setLiteralValue
argument_list|(
literal|"label"
argument_list|,
literal|"LABEL_NAME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fieldConfig
operator|.
name|getDefaultValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|annotation
operator|.
name|setLiteralValue
argument_list|(
literal|"defaultValue"
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"\"%s\""
argument_list|,
name|fieldConfig
operator|.
name|getDefaultValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fieldConfig
operator|.
name|isRequired
argument_list|()
condition|)
block|{
name|field
operator|.
name|addAnnotation
argument_list|(
name|Metadata
operator|.
name|class
argument_list|)
operator|.
name|setLiteralValue
argument_list|(
literal|"required"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|setSettersAndGettersMethods ()
specifier|private
name|void
name|setSettersAndGettersMethods
parameter_list|()
block|{
name|dbzConfigFields
operator|.
name|forEach
argument_list|(
parameter_list|(
name|fieldName
parameter_list|,
name|fieldConfig
parameter_list|)
lambda|->
block|{
if|if
condition|(
operator|!
name|isFieldInternalOrDeprecated
argument_list|(
name|fieldConfig
argument_list|)
condition|)
block|{
comment|// setters with javaDoc
name|javaClass
operator|.
name|addMethod
argument_list|()
operator|.
name|setName
argument_list|(
name|fieldConfig
operator|.
name|getFieldSetterMethodName
argument_list|()
argument_list|)
operator|.
name|addParameter
argument_list|(
name|fieldConfig
operator|.
name|getRawType
argument_list|()
argument_list|,
name|fieldConfig
operator|.
name|getFieldName
argument_list|()
argument_list|)
operator|.
name|setPublic
argument_list|()
operator|.
name|setReturnType
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
operator|.
name|setBody
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"this.%1$s = %1$s;"
argument_list|,
name|fieldConfig
operator|.
name|getFieldName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// getters
name|javaClass
operator|.
name|addMethod
argument_list|()
operator|.
name|setName
argument_list|(
name|fieldConfig
operator|.
name|getFieldGetterMethodName
argument_list|()
argument_list|)
operator|.
name|setPublic
argument_list|()
operator|.
name|setReturnType
argument_list|(
name|fieldConfig
operator|.
name|getRawType
argument_list|()
argument_list|)
operator|.
name|setBody
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"return %s;"
argument_list|,
name|fieldConfig
operator|.
name|getFieldName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|setCreateConnectorConfigurationMethod ()
specifier|private
name|void
name|setCreateConnectorConfigurationMethod
parameter_list|()
block|{
name|Method
name|createConfig
init|=
name|javaClass
operator|.
name|addMethod
argument_list|()
operator|.
name|setName
argument_list|(
literal|"createConnectorConfiguration"
argument_list|)
operator|.
name|setProtected
argument_list|()
operator|.
name|setReturnType
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
decl_stmt|;
name|createConfig
operator|.
name|addAnnotation
argument_list|(
name|Override
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// set config body
specifier|final
name|StringBuilder
name|stringBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|stringBuilder
operator|.
name|append
argument_list|(
literal|"final Configuration.Builder configBuilder = Configuration.create();\n\n"
argument_list|)
expr_stmt|;
name|dbzConfigFields
operator|.
name|forEach
argument_list|(
parameter_list|(
name|fieldName
parameter_list|,
name|fieldConfig
parameter_list|)
lambda|->
block|{
if|if
condition|(
operator|!
name|isFieldInternalOrDeprecated
argument_list|(
name|fieldConfig
argument_list|)
condition|)
block|{
name|stringBuilder
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"addPropertyIfNotNull(configBuilder, \"%s\", %s);\n"
argument_list|,
name|fieldConfig
operator|.
name|getRawName
argument_list|()
argument_list|,
name|fieldConfig
operator|.
name|getFieldName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|stringBuilder
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|stringBuilder
operator|.
name|append
argument_list|(
literal|"return configBuilder.build();"
argument_list|)
expr_stmt|;
name|createConfig
operator|.
name|setBody
argument_list|(
name|stringBuilder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setConfigureConnectorClassMethod ()
specifier|private
name|void
name|setConfigureConnectorClassMethod
parameter_list|()
block|{
name|javaClass
operator|.
name|addMethod
argument_list|()
operator|.
name|setName
argument_list|(
literal|"configureConnectorClass"
argument_list|)
operator|.
name|setProtected
argument_list|()
operator|.
name|setReturnType
argument_list|(
name|Class
operator|.
name|class
argument_list|)
operator|.
name|setBody
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"return %s.class;"
argument_list|,
name|connector
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|addAnnotation
argument_list|(
name|Override
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|setValidateConnectorConfiguration ()
specifier|private
name|void
name|setValidateConnectorConfiguration
parameter_list|()
block|{
comment|// validate config
name|Method
name|validateConfig
init|=
name|javaClass
operator|.
name|addMethod
argument_list|()
operator|.
name|setName
argument_list|(
literal|"validateConnectorConfiguration"
argument_list|)
operator|.
name|setReturnType
argument_list|(
name|ConfigurationValidation
operator|.
name|class
argument_list|)
operator|.
name|setProtected
argument_list|()
decl_stmt|;
comment|// set validate body
specifier|final
name|StringBuilder
name|stringBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|dbzConfigFields
operator|.
name|forEach
argument_list|(
parameter_list|(
name|fieldName
parameter_list|,
name|fieldConfig
parameter_list|)
lambda|->
block|{
if|if
condition|(
operator|!
name|isFieldInternalOrDeprecated
argument_list|(
name|fieldConfig
argument_list|)
operator|&&
name|fieldConfig
operator|.
name|isRequired
argument_list|()
condition|)
block|{
name|stringBuilder
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"if (isFieldValueNotSet(%s)) {\n"
argument_list|,
name|fieldConfig
operator|.
name|getFieldName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|stringBuilder
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"\treturn ConfigurationValidation.notValid(\"Required field '%s' must be set.\");\n}\n"
argument_list|,
name|fieldConfig
operator|.
name|getFieldName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|stringBuilder
operator|.
name|append
argument_list|(
literal|"return ConfigurationValidation.valid();"
argument_list|)
expr_stmt|;
name|validateConfig
operator|.
name|setBody
argument_list|(
name|stringBuilder
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|addAnnotation
argument_list|(
name|Override
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|isFieldInternalOrDeprecated (final ConnectorConfigField field)
specifier|private
name|boolean
name|isFieldInternalOrDeprecated
parameter_list|(
specifier|final
name|ConnectorConfigField
name|field
parameter_list|)
block|{
return|return
name|field
operator|.
name|isInternal
argument_list|()
operator|||
name|field
operator|.
name|isDeprecated
argument_list|()
return|;
block|}
block|}
end_class

end_unit

