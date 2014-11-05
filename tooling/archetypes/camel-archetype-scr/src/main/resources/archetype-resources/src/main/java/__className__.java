begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_expr_stmt
unit|#
name|set
argument_list|(
name|$symbol_pound
operator|=
literal|'#'
argument_list|)
expr|#
name|set
argument_list|(
name|$symbol_dollar
operator|=
literal|'$'
argument_list|)
expr|#
name|set
argument_list|(
name|$symbol_escape
operator|=
literal|'\' )
comment|// This file was generated from ${archetypeGroupId}/${archetypeArtifactId}/${archetypeVersion}
package|package
name|$
block|{
name|groupId
block|}
end_expr_stmt

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|scr
operator|.
name|AbstractCamelRunner
import|;
end_import

begin_import
import|import
name|$
block|{
name|groupId
block|}
end_import

begin_expr_stmt
operator|.
name|internal
operator|.
name|$
block|{
name|className
block|}
name|Route
expr_stmt|;
end_expr_stmt

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RoutesBuilder
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
name|ComponentResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|felix
operator|.
name|scr
operator|.
name|annotations
operator|.
name|*
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
name|List
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
name|label
operator|=
name|$
block|{
name|className
block|}
operator|.
name|COMPONENT_LABEL
argument_list|,
name|description
operator|=
name|$
block|{
name|className
block|}
operator|.
name|COMPONENT_DESCRIPTION
argument_list|,
name|immediate
operator|=
literal|true
argument_list|,
name|metatype
operator|=
literal|true
argument_list|)
annotation|@
name|Properties
argument_list|(
block|{
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"camelContextId"
argument_list|,
name|value
operator|=
literal|"${artifactId}"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"camelRouteId"
argument_list|,
name|value
operator|=
literal|"foo/timer-log"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"active"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"from"
argument_list|,
name|value
operator|=
literal|"timer:foo?period=5000"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"to"
argument_list|,
name|value
operator|=
literal|"log:foo?showHeaders=true"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"summaryLogging"
argument_list|,
name|value
operator|=
literal|"false"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"messageOk"
argument_list|,
name|value
operator|=
literal|"Success: {{from}} -> {{to}}"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"messageError"
argument_list|,
name|value
operator|=
literal|"Failure: {{from}} -> {{to}}"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"maximumRedeliveries"
argument_list|,
name|value
operator|=
literal|"0"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"redeliveryDelay"
argument_list|,
name|value
operator|=
literal|"5000"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"backOffMultiplier"
argument_list|,
name|value
operator|=
literal|"2"
argument_list|)
block|,
annotation|@
name|Property
argument_list|(
name|name
operator|=
literal|"maximumRedeliveryDelay"
argument_list|,
name|value
operator|=
literal|"60000"
argument_list|)
block|}
argument_list|)
annotation|@
name|References
argument_list|(
block|{
annotation|@
name|Reference
argument_list|(
name|name
operator|=
literal|"camelComponent"
argument_list|,
name|referenceInterface
operator|=
name|ComponentResolver
operator|.
name|class
argument_list|,
name|cardinality
operator|=
name|ReferenceCardinality
operator|.
name|MANDATORY_MULTIPLE
argument_list|,
name|policy
operator|=
name|ReferencePolicy
operator|.
name|DYNAMIC
argument_list|,
name|policyOption
operator|=
name|ReferencePolicyOption
operator|.
name|GREEDY
argument_list|,
name|bind
operator|=
literal|"gotCamelComponent"
argument_list|,
name|unbind
operator|=
literal|"lostCamelComponent"
argument_list|)
block|}
argument_list|)
DECL|class|$
specifier|public
class|class
name|$
block|{
name|className
block|}
end_class

begin_expr_stmt
DECL|class|$
unit|extends
name|AbstractCamelRunner
block|{
specifier|public
specifier|static
name|final
name|String
name|COMPONENT_LABEL
operator|=
literal|"${groupId}.${className}"
block|;
specifier|public
specifier|static
name|final
name|String
name|COMPONENT_DESCRIPTION
operator|=
literal|"This is the current configuration for ${artifactId}."
block|;      @
name|Override
specifier|protected
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|getRouteBuilders
argument_list|()
block|{
name|List
argument_list|<
name|RoutesBuilder
argument_list|>
name|routesBuilders
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
block|;
name|routesBuilders
operator|.
name|add
argument_list|(
operator|new
name|$
block|{
name|className
block|}
name|Route
argument_list|(
name|registry
argument_list|)
argument_list|)
block|;
return|return
name|routesBuilders
return|;
block|}
end_expr_stmt

unit|}
end_unit

